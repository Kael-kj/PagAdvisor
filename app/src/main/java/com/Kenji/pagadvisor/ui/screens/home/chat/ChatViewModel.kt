package com.Kenji.pagadvisor.ui.screens.home.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.domain.repository.SalesRepository
import com.Kenji.pagadvisor.domain.usecase.GetAiAnalysisUseCase
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetWeeklyGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Representa o estado da UI para a tela de Chat.
 *
 * @property messages A lista de mensagens a serem exibidas na conversa.
 * @property isLoading `true` se uma resposta da IA estiver sendo carregada, `false` caso contrário.
 * @property suggestedReplies Uma lista de respostas sugeridas (extraídas da resposta da IA) para serem exibidas como chips.
 * @property isAwaitingPlanConfirmation `true` se a última mensagem da IA foi uma pergunta ou sugeriu um plano, indicando que a próxima mensagem do usuário deve ser enviada com contexto.
 */
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val suggestedReplies: List<String> = emptyList(),
    val isAwaitingPlanConfirmation: Boolean = false
)

/**
 * ViewModel para a tela de Chat.
 *
 * Gerencia o estado da conversa, envia as perguntas do usuário para a IA, processa as respostas
 * e atualiza a UI de acordo.
 */
class ChatViewModel(
    private val getAiAnalysisUseCase: GetAiAnalysisUseCase,
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val getSalesGoalUseCase: GetWeeklyGoalUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    private var lastAiMessage: String? = null // Armazena a última resposta da IA para uso como contexto.

    init {
        // Adiciona uma mensagem de boas-vindas inicial à conversa.
        _uiState.update {
            it.copy(messages = listOf(ChatMessage("Olá! Sou o PagAdvisor. Pergunte-me sobre suas vendas!", isFromUser = false)))
        }
    }

    /**
     * Processa e envia a mensagem do usuário para a IA.
     *
     * @param userQuery O texto da mensagem enviada pelo usuário.
     */
    fun onSendMessage(userQuery: String) {
        if (userQuery.isBlank() || _uiState.value.isLoading) return

        val userMessage = ChatMessage(userQuery, isFromUser = true)
        val currentState = _uiState.value
        val isReply = currentState.suggestedReplies.any { it == userQuery } // Verifica se a mensagem é um clique em um chip de sugestão.

        // Define o contexto se a IA estiver aguardando uma confirmação (ex: "Sim") ou se for uma resposta a uma sugestão.
        val context = if (currentState.isAwaitingPlanConfirmation || isReply) {
            lastAiMessage
        } else {
            null
        }

        // Atualiza a UI para mostrar a mensagem do usuário e o indicador de loading.
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                suggestedReplies = emptyList(), // Limpa as sugestões antigas.
                isAwaitingPlanConfirmation = false // Reseta o estado de confirmação.
            )
        }

        viewModelScope.launch {
            try {
                // 1. Coleta todos os dados necessários para o contexto da IA.
                val sales = getMockedSalesUseCase()
                val weeklyGoal = getSalesGoalUseCase().first()
                val dailyGoal = salesRepository.getDailyGoal().first()
                val profile = getUserProfileUseCase().first()

                // 2. Monta o objeto de requisição para a API.
                val request = AnalysisRequest(
                    userQuery = userQuery,
                    salesGoal = weeklyGoal,
                    weeklySales = sales,
                    businessType = profile.businessType,
                    businessProducts = profile.businessProducts.toList(),
                    dailyGoal = dailyGoal,
                    context = context
                )

                // 3. Envia a requisição e recebe a resposta da IA.
                val response = getAiAnalysisUseCase(request)
                val aiMessageText = response.aiReply
                val aiMessage = ChatMessage(aiMessageText, isFromUser = false)

                lastAiMessage = aiMessageText // Salva a resposta para possível uso como contexto futuro.

                // 4. Extrai quaisquer planos de ação da resposta para usar como chips de sugestão.
                val plans = parsePlansFromResponse(aiMessageText)

                // 5. Atualiza a UI com a resposta da IA e as novas sugestões.
                _uiState.update {
                    it.copy(
                        messages = it.messages + aiMessage,
                        isLoading = false,
                        suggestedReplies = plans,
                        isAwaitingPlanConfirmation = plans.isNotEmpty() || aiMessageText.endsWith("?")
                    )
                }
            } catch (e: Exception) {
                // Em caso de erro, exibe uma mensagem de falha na UI.
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
     * Usa Regex para encontrar e extrair "planos" da resposta da IA, formatados como `[PLANO: ...]`, 
     * para serem usados como chips de resposta sugerida.
     */
    private fun parsePlansFromResponse(text: String): List<String> {
        val regex = Regex("\\[PLANO: (.*?)\\]")
        return regex.findAll(text).map { it.groupValues[1] }.toList()
    }
}