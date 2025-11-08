package com.Kenji.pagadvisor.ui.screens.home.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetSalesGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.SetSalesGoalUseCase
import com.Kenji.pagadvisor.domain.repository.SalesRepository // 👈 Importe o Repositório
import com.Kenji.pagadvisor.ui.screens.home.dashboard.toCurrency
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Função helper para arredondar
private fun Double.roundToNearest(value: Int): Double {
    return ((this / value).toInt() * value).toDouble()
}

data class GoalUiState(
    val currentWeeklyGoal: Double = 0.0,
    val suggestedGoal: Double = 0.0,
    val sliderValue: Float = 0f,
    val snackbarMessage: String? = null
)

class GoalViewModel(
    private val getSalesGoalUseCase: GetSalesGoalUseCase,
    private val setSalesGoalUseCase: SetSalesGoalUseCase,
    // 👇 Precisamos do SalesRepository (via Factory) para os UseCases
    // (Vamos atualizar a Factory no próximo passo)
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val salesRepository: SalesRepository // Para salvar a meta diária
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialState()
    }

    private fun loadInitialState() {
        viewModelScope.launch {
            // Pega a meta semanal salva
            getSalesGoalUseCase().collect { savedGoal ->
                if (savedGoal == 0.0) {
                    // SE NÃO TIVER META: Sugere uma nova
                    val pastSales = getMockedSalesUseCase().sumOf { it.totalSold }
                    val suggestion = (pastSales * 1.1).roundToNearest(100) // Sugere 10% a mais, arredondado
                    _uiState.update {
                        it.copy(
                            currentWeeklyGoal = 0.0,
                            suggestedGoal = suggestion,
                            sliderValue = suggestion.toFloat()
                        )
                    }
                } else {
                    // SE JÁ TIVER META: Apenas carrega
                    _uiState.update {
                        it.copy(
                            currentWeeklyGoal = savedGoal,
                            suggestedGoal = 0.0,
                            sliderValue = savedGoal.toFloat()
                        )
                    }
                }
            }
        }
    }

    fun onSliderValueChanged(newValue: Float) {
        _uiState.update { it.copy(sliderValue = newValue) }
    }

    fun onSaveGoalClicked() {
        val newGoal = _uiState.value.sliderValue.toDouble()
        if (newGoal > 0) {
            viewModelScope.launch {
                // 1. Salva a meta semanal
                setSalesGoalUseCase(newGoal)

                // 2. Calcula e salva a meta diária (dividido por 7)
                val dailyGoal = (newGoal / 7.0).roundToNearest(10)
                salesRepository.setDailyGoal(dailyGoal)

                _uiState.update {
                    it.copy(
                        currentWeeklyGoal = newGoal,
                        snackbarMessage = "Meta de ${newGoal.toCurrency()} salva!"
                    )
                }
            }
        }
    }

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}