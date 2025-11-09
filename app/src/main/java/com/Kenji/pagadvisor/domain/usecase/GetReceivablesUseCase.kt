package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

/**
 * Caso de uso (Use Case) para obter o valor total a receber.
 *
 * Esta classe abstrai a lógica de negócio para buscar o valor de contas a receber
 * do repositório, seguindo os princípios da Clean Architecture.
 *
 * @property repository O repositório de vendas que provê os dados.
 */
class GetReceivablesUseCase(private val repository: SalesRepository) {

    /**
     * Executa o caso de uso.
     *
     * @return O valor total a receber como um [Double].
     */
    suspend operator fun invoke(): Double {
        return repository.getReceivables()
    }
}