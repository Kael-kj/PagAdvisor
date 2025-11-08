package com.Kenji.pagadvisor.ui.screens.home.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.data.remote.dto.DailySale
import com.Kenji.pagadvisor.domain.repository.SalesRepository // 👈 IMPORTADO
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetSalesGoalUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val salesGoal: Double = 0.0,
    val dailyGoal: Double = 0.0, // 👈 NOVO
    val salesList: List<DailySale> = emptyList(),
    val totalSales: Double = 0.0,
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val getSalesGoalUseCase: GetSalesGoalUseCase,
    private val salesRepository: SalesRepository // 👈 DEPENDÊNCIA ADICIONADA
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Combina as TRÊS fontes de dados (vendas, meta semanal, meta diária)
            combine(
                getSalesGoalUseCase(),
                salesRepository.getDailyGoal(), // 👈 NOVO
                flow { emit(getMockedSalesUseCase()) }
            ) { weeklyGoal, dailyGoal, sales ->
                DashboardUiState(
                    salesGoal = weeklyGoal,
                    dailyGoal = dailyGoal, // 👈 NOVO
                    salesList = sales,
                    totalSales = sales.sumOf { it.totalSold },
                    isLoading = false
                )
            }.catch {
                _uiState.update { it.copy(isLoading = false) }
            }.collect { combinedState ->
                _uiState.value = combinedState
            }
        }
    }
}