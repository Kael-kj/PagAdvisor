package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

// Um "data class" para agrupar os dados do perfil
data class UserProfile(
    val businessName: String = "",
    val businessType: String = "",
    val businessProducts: Set<String> = emptySet()
)

class GetUserProfileUseCase(private val repository: SalesRepository) {

    // Este UseCase combina os 3 flows de dados em um único objeto UserProfile
    operator fun invoke(): Flow<UserProfile> {
        return combine(
            repository.getBusinessName(),
            repository.getBusinessType(),
            repository.getBusinessProducts()
        ) { name, type, products ->
            UserProfile(name, type, products)
        }
    }
}