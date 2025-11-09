package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

class LogoutUseCase(private val repository: SalesRepository) {
    suspend operator fun invoke() {
        repository.logout()
    }
}