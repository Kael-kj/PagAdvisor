package com.Kenji.pagadvisor.data.repository

import com.Kenji.pagadvisor.data.local.UserPreferencesRepository
import com.Kenji.pagadvisor.data.remote.dto.DailySale
import com.Kenji.pagadvisor.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow

// Vamos precisar injetar o UserPreferencesRepository (via Hilt ou manualmente)
// Por enquanto, vamos assumir que ele será injetado
class SalesRepositoryImpl(
    private val preferences: UserPreferencesRepository
) : SalesRepository {

    override suspend fun getMockedSales(): List<DailySale> {
        // AQUI ESTÁ NOSSO MOCK!
        return listOf(
            DailySale(dayOfWeek = "Seg", totalSold = 350.75),
            DailySale(dayOfWeek = "Ter", totalSold = 420.50),
            DailySale(dayOfWeek = "Qua", totalSold = 290.00),
            DailySale(dayOfWeek = "Qui", totalSold = 510.20),
            DailySale(dayOfWeek = "Sex", totalSold = 730.80),
            DailySale(dayOfWeek = "Sab", totalSold = 150.25),
            DailySale(dayOfWeek = "Dom", totalSold = 80.00)
        )
    }

    override suspend fun setSalesGoal(goal: Double) {
        preferences.setSalesGoal(goal)
    }

    override fun getSalesGoal(): Flow<Double> {
        return preferences.getSalesGoal()
    }
    override suspend fun saveUserProfile(name: String, type: String, products: Set<String>) {
        preferences.saveUserProfile(name, type, products)
    }

    override fun getBusinessName(): Flow<String> {
        return preferences.getBusinessName()
    }

    override fun getBusinessType(): Flow<String> {
        return preferences.getBusinessType()
    }

    override fun getBusinessProducts(): Flow<Set<String>> {
        return preferences.getBusinessProducts()
    }
    override suspend fun setDailyGoal(dailyGoal: Double) {
        preferences.setDailyGoal(dailyGoal)
    }

    override fun getDailyGoal(): Flow<Double> {
        return preferences.getDailyGoal()
    }
}