package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.data.remote.dto.DailySale
import com.Kenji.pagadvisor.domain.repository.SalesRepository

class GetMockedSalesUseCase(private val repository: SalesRepository) {
    suspend operator fun invoke(): List<DailySale> {
        return repository.getMockedSales()
    }
}