package com.Kenji.pagadvisor.ui.screens.home.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.domain.repository.SalesRepository // 👈 IMPORTADO
import com.Kenji.pagadvisor.domain.usecase.GetAiAnalysisUseCase
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetWeeklyGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.GetUserProfileUseCase // 👈 IMPORTADO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val suggestedReplies: List<String> = emptyList(),
    val isAwaitingPlanConfirmation: Boolean = false
)

class ChatViewModel(
    private val getAiAnalysisUseCase: GetAiAnalysisUseCase,
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val getSalesGoalUseCase: GetWeeklyGoalUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    private var lastAiMessage: String? = null

    init {
        _uiState.update {
            it.copy(messages = listOf(ChatMessage("Olá! Sou o PagAdvisor. Pergunte-me sobre suas vendas!", isFromUser = false)))
        }
    }

    fun onSendMessage(userQuery: String) {
        if (userQuery.isBlank() || _uiState.value.isLoading) return

        val userMessage = ChatMessage(userQuery, isFromUser = true)

        val currentState = _uiState.value
        val isReply = currentState.suggestedReplies.any { it == userQuery }

        // Se a IA estava esperando uma confirmação (ex: "sim")
        // OU se o usuário clicou em um chip, nós enviamos o contexto.
        val context = if (currentState.isAwaitingPlanConfirmation || isReply) {
            lastAiMessage
        } else {
            null
        }

        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                suggestedReplies = emptyList(), // Limpa os chips
                isAwaitingPlanConfirmation = false // Reseta a memória
            )
        }

        viewModelScope.launch {
            try {
                // --- LÓGICA V2: Busca todos os dados ---
                val sales = getMockedSalesUseCase()
                val weeklyGoal = getSalesGoalUseCase().first()
                val dailyGoal = salesRepository.getDailyGoal().first()
                val profile = getUserProfileUseCase().first()

                val context = if (isReply) lastAiMessage else null

                // Monta a nova requisição (v2)
                val request = AnalysisRequest(
                    userQuery = userQuery,
                    salesGoal = weeklyGoal,
                    weeklySales = sales,
                    businessType = profile.businessType,
                    businessProducts = profile.businessProducts.toList(),
                    dailyGoal = dailyGoal,
                    context = context
                )

                // 3. Recebe a resposta da IA
                val response = getAiAnalysisUseCase(request)
                val aiMessageText = response.aiReply
                val aiMessage = ChatMessage(aiMessageText, isFromUser = false)

                lastAiMessage = aiMessageText

                val plans = parsePlansFromResponse(aiMessageText)

                _uiState.update {
                    it.copy(
                        messages = it.messages + aiMessage,
                        isLoading = false,
                        suggestedReplies = plans,
                        isAwaitingPlanConfirmation = plans.isNotEmpty() || aiMessageText.endsWith("?")
                    )
                }
            } catch (e: Exception) {
                val errorMessage = ChatMessage("Desculpe, ocorreu um erro. Tente novamente. (${e.message})", isFromUser = false)
                _uiState.update {
                    it.copy(
                        messages = it.messages + errorMessage,
                        isLoading = false
                    )
                }
            }
        }
    }
    /**
     * Procura por [PLANO: ...] na resposta da IA usando Regex.
     */
    private fun parsePlansFromResponse(text: String): List<String> {
        val regex = Regex("\\[PLANO: (.*?)\\]")
        return regex.findAll(text).map { it.groupValues[1] }.toList()
    }
}