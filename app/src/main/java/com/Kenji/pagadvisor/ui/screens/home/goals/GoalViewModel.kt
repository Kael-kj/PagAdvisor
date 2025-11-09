package com.Kenji.pagadvisor.ui.screens.home.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.domain.repository.SalesRepository
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetWeeklyGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.SetWeeklyGoalUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Função helper para arredondar
private fun Double.roundToNearest(value: Int): Double {
    return ((this / value).toInt() * value).toDouble()
}

// Define os tipos de meta
enum class GoalType { Semanal, Mensal, Anual }

data class GoalUiState(
    val selectedTab: GoalType = GoalType.Semanal, // Aba atual
    val weeklyGoal: Double = 0.0,
    val monthlyGoal: Double = 0.0,
    val annualGoal: Double = 0.0,
    val suggestedGoal: Double = 0.0, // Sugestão para a aba selecionada
    val sliderValue: Float = 0f,
    val snackbarMessage: String? = null
)

class GoalViewModel(
    private val getWeeklyGoalUseCase: GetWeeklyGoalUseCase,
    private val setWeeklyGoalUseCase: SetWeeklyGoalUseCase,
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val salesRepository: SalesRepository // Acesso total para todas as metas
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Carrega TODOS os tipos de meta quando o ViewModel é iniciado
        loadAllGoals()
        // Define a aba padrão (Semanal) e gera sugestão se necessário
        onTabSelected(GoalType.Semanal)
    }

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
            }.collect() // Coleta o flow combinado
        }
    }

    fun onTabSelected(goalType: GoalType) {
        _uiState.update { it.copy(selectedTab = goalType) }

        // Atualiza o slider e a sugestão para a aba selecionada
        viewModelScope.launch {
            val currentState = _uiState.value
            val currentGoal = when (goalType) {
                GoalType.Semanal -> currentState.weeklyGoal
                GoalType.Mensal -> currentState.monthlyGoal
                GoalType.Anual -> currentState.annualGoal
            }

            if (currentGoal == 0.0) {
                // Se não tiver meta para esta aba, sugere uma
                val pastSales = getMockedSalesUseCase().sumOf { it.totalSold }
                val suggestion = when (goalType) {
                    GoalType.Semanal -> (pastSales * 1.1).roundToNearest(100)
                    GoalType.Mensal -> (pastSales * 4 * 1.1).roundToNearest(500)
                    GoalType.Anual -> (pastSales * 52 * 1.1).roundToNearest(1000)
                }
                _uiState.update { it.copy(suggestedGoal = suggestion, sliderValue = suggestion.toFloat()) }
            } else {
                // Se já tiver meta, apenas carrega
                _uiState.update { it.copy(suggestedGoal = 0.0, sliderValue = currentGoal.toFloat()) }
            }
        }
    }

    fun onSliderValueChanged(newValue: Float) {
        _uiState.update { it.copy(sliderValue = newValue) }
    }

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

                _uiState.update {
                    it.copy(snackbarMessage = "Meta ${currentTab.name} de ${newGoal.toCurrency()} salva!")
                }
            }
        }
    }

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}