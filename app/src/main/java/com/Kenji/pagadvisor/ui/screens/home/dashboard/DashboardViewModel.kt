package com.Kenji.pagadvisor.ui.screens.home.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.data.remote.dto.DailySale
import com.Kenji.pagadvisor.domain.repository.SalesRepository
import com.Kenji.pagadvisor.domain.usecase.GetMockedSalesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetWeeklyGoalUseCase
import com.Kenji.pagadvisor.domain.usecase.GetReceivablesUseCase
import com.Kenji.pagadvisor.domain.usecase.GetCustomerStatsUseCase
import com.Kenji.pagadvisor.domain.usecase.CustomerStats
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val salesGoal: Double = 0.0,
    val dailyGoal: Double = 0.0,
    val salesList: List<DailySale> = emptyList(),
    val totalSales: Double = 0.0,
    val receivables: Double = 0.0,
    val customerStats: CustomerStats = CustomerStats(0, 0),
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val getMockedSalesUseCase: GetMockedSalesUseCase,
    private val getSalesGoalUseCase: GetWeeklyGoalUseCase,
    private val salesRepository: SalesRepository,
    private val getReceivablesUseCase: GetReceivablesUseCase,
    private val getCustomerStatsUseCase: GetCustomerStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                getSalesGoalUseCase(),
                salesRepository.getDailyGoal(),
                flow { emit(getMockedSalesUseCase()) },
                flow { emit(getReceivablesUseCase()) },
                flow { emit(getCustomerStatsUseCase()) }
            ) { weeklyGoal, dailyGoal, sales, receivables, customers ->
                DashboardUiState(
                    salesGoal = weeklyGoal,
                    dailyGoal = dailyGoal,
                    salesList = sales,
                    totalSales = sales.sumOf { it.totalSold },
                    receivables = receivables,
                    customerStats = customers,
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