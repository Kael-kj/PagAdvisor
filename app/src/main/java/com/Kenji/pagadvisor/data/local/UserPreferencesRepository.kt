package com.Kenji.pagadvisor.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    // --- Chaves de Preferência ---
    private val WEEKLY_GOAL_KEY = doublePreferencesKey("sales_goal") // Renomeado para clareza
    private val DAILY_GOAL_KEY = doublePreferencesKey("daily_goal")
    private val MONTHLY_GOAL_KEY = doublePreferencesKey("monthly_goal") // 👈 NOVO
    private val ANNUAL_GOAL_KEY = doublePreferencesKey("annual_goal") // 👈 NOVO

    private val BUSINESS_NAME_KEY = stringPreferencesKey("business_name")
    private val BUSINESS_TYPE_KEY = stringPreferencesKey("business_type")
    private val BUSINESS_PRODUCTS_KEY = stringSetPreferencesKey("business_products")

    // --- Funções de Metas ---
    suspend fun setWeeklyGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[WEEKLY_GOAL_KEY] = goal
        }
    }

    fun getWeeklyGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[WEEKLY_GOAL_KEY] ?: 0.0
        }
    }

    suspend fun setDailyGoal(dailyGoal: Double) {
        dataStore.edit { preferences ->
            preferences[DAILY_GOAL_KEY] = dailyGoal
        }
    }

    fun getDailyGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[DAILY_GOAL_KEY] ?: 0.0
        }
    }

    suspend fun setMonthlyGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[MONTHLY_GOAL_KEY] = goal
        }
    }

    fun getMonthlyGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[MONTHLY_GOAL_KEY] ?: 0.0
        }
    }

    suspend fun setAnnualGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[ANNUAL_GOAL_KEY] = goal
        }
    }

    fun getAnnualGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[ANNUAL_GOAL_KEY] ?: 0.0
        }
    }

    // --- Funções de Perfil (sem alteração) ---
    suspend fun saveUserProfile(name: String, type: String, products: Set<String>) {
        dataStore.edit { preferences ->
            preferences[BUSINESS_NAME_KEY] = name
            preferences[BUSINESS_TYPE_KEY] = type
            preferences[BUSINESS_PRODUCTS_KEY] = products
        }
    }
    fun getBusinessName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUSINESS_NAME_KEY] ?: ""
        }
    }
    fun getBusinessType(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUSINESS_TYPE_KEY] ?: ""
        }
    }
    fun getBusinessProducts(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[BUSINESS_PRODUCTS_KEY] ?: emptySet()
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}