package com.Kenji.pagadvisor.ui.screens.home.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.domain.repository.SalesRepository
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetWeeklyGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.SetWeeklyGoalUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Função de extensão para arredondar um [Double] para o múltiplo mais próximo de um valor inteiro.
 * Ex: `123.45.roundToNearest(10)` retorna `120.0`.
 */
private fun Double.roundToNearest(value: Int): Double {
    return ((this / value).toInt() * value).toDouble()
}

/**
 * Enumeração para os diferentes períodos de metas que o usuário pode definir.
 */
enum class GoalType { Semanal, Mensal, Anual }

/**
 * Representa o estado da UI para a tela de Metas.
 *
 * @property selectedTab A aba de período de meta atualmente selecionada (Semanal, Mensal, Anual).
 * @property weeklyGoal O valor da meta semanal salva.
 * @property monthlyGoal O valor da meta mensal salva.
 * @property annualGoal O valor da meta anual salva.
 * @property suggestedGoal O valor de meta sugerido pela IA caso nenhuma meta esteja definida para a aba atual.
 * @property sliderValue O valor atual do slider de seleção de meta.
 * @property snackbarMessage A mensagem a ser exibida em um Snackbar (ou null se não houver mensagem).
 */
data class GoalUiState(
    val selectedTab: GoalType = GoalType.Semanal,
    val weeklyGoal: Double = 0.0,
    val monthlyGoal: Double = 0.0,
    val annualGoal: Double = 0.0,
    val suggestedGoal: Double = 0.0,
    val sliderValue: Float = 0f,
    val snackbarMessage: String? = null
)

/**
 * ViewModel para a tela de Metas.
 *
 * Gerencia o estado da UI (`GoalUiState`), lida com a lógica de negócio para carregar, 
 * sugerir e salvar metas de vendas para diferentes períodos.
 */
class GoalViewModel(
    private val getWeeklyGoalUseCase: GetWeeklyGoalUseCase,
    private val setWeeklyGoalUseCase: SetWeeklyGoalUseCase,
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Carrega todas as metas salvas (semanal, mensal, anual) ao iniciar o ViewModel.
        loadAllGoals()
        // Seleciona a aba "Semanal" como padrão e gera uma sugestão se necessário.
        onTabSelected(GoalType.Semanal)
    }

    /**
     * Carrega os valores de todas as metas (semanal, mensal, anual) do repositório
     * e atualiza o estado da UI de forma reativa.
     */
    private fun loadAllGoals() {
        viewModelScope.launch {
            combine(
                getWeeklyGoalUseCase(),
                salesRepository.getMonthlyGoal(),
                salesRepository.getAnnualGoal()
            ) { weekly, monthly, annual ->
                _uiState.update {
                    it.copy(
                        weeklyGoal = weekly,
                        monthlyGoal = monthly,
                        annualGoal = annual
                    )
                }
            }.collect() 
        }
    }

    /**
     * Chamado quando o usuário seleciona uma nova aba de período de meta.
     * Atualiza a aba selecionada e gera uma sugestão de meta se não houver uma definida.
     */
    fun onTabSelected(goalType: GoalType) {
        _uiState.update { it.copy(selectedTab = goalType) }

        viewModelScope.launch {
            val currentState = _uiState.value
            val currentGoal = when (goalType) {
                GoalType.Semanal -> currentState.weeklyGoal
                GoalType.Mensal -> currentState.monthlyGoal
                GoalType.Anual -> currentState.annualGoal
            }

            if (currentGoal == 0.0) {
                // Se não há meta definida para este período, calcula uma sugestão.
                val pastSales = getMockedSalesUseCase().sumOf { it.totalSold }
                val suggestion = when (goalType) {
                    GoalType.Semanal -> (pastSales * 1.1).roundToNearest(100)
                    GoalType.Mensal -> (pastSales * 4 * 1.1).roundToNearest(500)
                    GoalType.Anual -> (pastSales * 52 * 1.1).roundToNearest(1000)
                }
                _uiState.update { it.copy(suggestedGoal = suggestion, sliderValue = suggestion.toFloat()) }
            } else {
                // Se já existe uma meta, carrega seu valor no slider.
                _uiState.update { it.copy(suggestedGoal = 0.0, sliderValue = currentGoal.toFloat()) }
            }
        }
    }

    /**
     * Atualiza o valor do slider no estado da UI conforme o usuário o arrasta.
     */
    fun onSliderValueChanged(newValue: Float) {
        _uiState.update { it.copy(sliderValue = newValue) }
    }

    /**
     * Salva o valor atual do slider como a nova meta para o período selecionado.
     * Também calcula e salva a meta diária se a meta semanal for definida.
     */
    fun onSaveGoalClicked() {
        val newGoal = _uiState.value.sliderValue.toDouble()
        val currentTab = _uiState.value.selectedTab

        if (newGoal > 0) {
            viewModelScope.launch {
                when (currentTab) {
                    GoalType.Semanal -> {
                        setWeeklyGoalUseCase(newGoal)
                        val dailyGoal = (newGoal / 7.0).roundToNearest(10)
                        salesRepository.setDailyGoal(dailyGoal)
                    }
                    GoalType.Mensal -> salesRepository.setMonthlyGoal(newGoal)
                    GoalType.Anual -> salesRepository.setAnnualGoal(newGoal)
                }

                // Exibe uma mensagem de confirmação.
                _uiState.update {
                    it.copy(snackbarMessage = "Meta ${currentTab.name} de R$${newGoal} salva!")
                }
            }
        }
    }

    /**
     * Limpa a mensagem do Snackbar do estado da UI depois que ela foi exibida.
     */
    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}