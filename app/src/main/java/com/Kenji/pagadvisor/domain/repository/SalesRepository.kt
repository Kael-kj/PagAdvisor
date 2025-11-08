package com.Kenji.pagadvisor.domain.repository

import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    // Retorna uma lista de vendas FAKE
    suspend fun getMockedSales(): List<com.Kenji.pagadvisor.data.remote.dto.DailySale>

    // Salva a meta do usuário
    suspend fun setSalesGoal(goal: Double)

    // Obtém a meta do usuário
    fun getSalesGoal(): Flow<Double>
    suspend fun saveUserProfile(name: String, type: String, products: Set<String>)
    fun getBusinessName(): Flow<String>
    fun getBusinessType(): Flow<String>
    fun getBusinessProducts(): Flow<Set<String>>
    suspend fun setDailyGoal(dailyGoal: Double)
    fun getDailyGoal(): Flow<Double>
}