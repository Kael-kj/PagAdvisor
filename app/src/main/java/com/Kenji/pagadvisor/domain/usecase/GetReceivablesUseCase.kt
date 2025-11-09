package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

class GetReceivablesUseCase(private val repository: SalesRepository) {
    suspend operator fun invoke(): Double {
        return repository.getReceivables()
    }
}