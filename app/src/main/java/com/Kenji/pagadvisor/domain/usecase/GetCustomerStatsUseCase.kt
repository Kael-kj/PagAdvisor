package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

/**
 * Data class para encapsular as estatísticas de clientes.
 *
 * @property totalCustomers O número total de clientes cadastrados.
 * @property newCustomersThisWeek O número de novos clientes registrados na semana atual.
 */
data class CustomerStats(
    val totalCustomers: Int,
    val newCustomersThisWeek: Int
)

/**
 * Caso de uso para obter as estatísticas de clientes.
 *
 * @property repository O repositório de vendas que fornece os dados.
 */
class GetCustomerStatsUseCase(private val repository: SalesRepository) {

    /**
     * Executa o caso de uso para buscar as estatísticas de clientes.
     *
     * @return Um objeto [CustomerStats] contendo os dados dos clientes.
     */
    suspend operator fun invoke(): CustomerStats {
        return repository.getCustomerStats()
    }
}