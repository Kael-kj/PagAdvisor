package com.Kenji.pagadvisor.domain.repository

import kotlinx.coroutines.flow.Flow
import com.Kenji.pagadvisor.domain.usecase.CustomerStats

interface SalesRepository {
    // Retorna uma lista de vendas FAKE
    suspend fun getMockedSales(): List<com.Kenji.pagadvisor.data.remote.dto.DailySale>
    suspend fun saveUserProfile(name: String, type: String, products: Set<String>)
    fun getBusinessName(): Flow<String>
    fun getBusinessType(): Flow<String>
    fun getBusinessProducts(): Flow<Set<String>>
    suspend fun setDailyGoal(dailyGoal: Double)
    fun getDailyGoal(): Flow<Double>
    suspend fun setWeeklyGoal(goal: Double)
    fun getWeeklyGoal(): Flow<Double>
    suspend fun setMonthlyGoal(goal: Double)
    fun getMonthlyGoal(): Flow<Double>
    suspend fun setAnnualGoal(goal: Double)
    fun getAnnualGoal(): Flow<Double>
    suspend fun getReceivables(): Double
    suspend fun getCustomerStats(): CustomerStats
    suspend fun logout()
}