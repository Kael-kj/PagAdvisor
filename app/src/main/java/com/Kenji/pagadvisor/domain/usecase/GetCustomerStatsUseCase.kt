package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

// Data class para os dados de clientes
data class CustomerStats(
    val totalCustomers: Int,
    val newCustomersThisWeek: Int
)

class GetCustomerStatsUseCase(private val repository: SalesRepository) {
    suspend operator fun invoke(): CustomerStats {
        return repository.getCustomerStats()
    }
}