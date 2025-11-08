package com.Kenji.pagadvisor.ui.screens.home.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.domain.repository.SalesRepository // 👈 IMPORTADO
import com.Kenji.pagadvisor.domain.usecase.GetAiAnalysisUseCase
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetSalesGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.GetUserProfileUseCase // 👈 IMPORTADO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)

class ChatViewModel(
    private val getAiAnalysisUseCase: GetAiAnalysisUseCase,
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val getSalesGoalUseCase: GetSalesGoalUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase, // 👈 DEPENDÊNCIA ADICIONADA
    private val salesRepository: SalesRepository // 👈 DEPENDÊNCIA ADICIONADA
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(messages = listOf(ChatMessage("Olá! Sou o PagAdvisor. Pergunte-me sobre suas vendas!", isFromUser = false)))
        }
    }

    fun onSendMessage(userQuery: String) {
        if (userQuery.isBlank() || _uiState.value.isLoading) return

        val userMessage = ChatMessage(userQuery, isFromUser = true)
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true
            )
        }

        viewModelScope.launch {
            try {
                // --- LÓGICA V2: Busca todos os dados ---
                val sales = getMockedSalesUseCase()
                val weeklyGoal = getSalesGoalUseCase().first()
                val dailyGoal = salesRepository.getDailyGoal().first() // 👈 NOVO
                val profile = getUserProfileUseCase().first() // 👈 NOVO

                // Monta a nova requisição (v2)
                val request = AnalysisRequest(
                    userQuery = userQuery,
                    salesGoal = weeklyGoal,
                    weeklySales = sales,
                    businessType = profile.businessType, // 👈 NOVO
                    businessProducts = profile.businessProducts.toList(), // 👈 NOVO
                    dailyGoal = dailyGoal // 👈 NOVO
                )

                // 3. Recebe a resposta da IA
                val response = getAiAnalysisUseCase(request)
                val aiMessage = ChatMessage(response.aiReply, isFromUser = false)

                _uiState.update {
                    it.copy(
                        messages = it.messages + aiMessage,
                        isLoading = false
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
}