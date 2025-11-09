package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

// Nota: A lógica de salvar/ler perfil está no SalesRepository
class SaveUserProfileUseCase(private val repository: SalesRepository) {

    suspend operator fun invoke(
        name: String,
        type: String,
        products: Set<String>
    ) {
        repository.saveUserProfile(name, type, products)
    }
}