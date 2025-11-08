package com.Kenji.pagadvisor.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensão para criar o DataStore
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    // --- Chaves de Preferência ---
    private val SALES_GOAL_KEY = doublePreferencesKey("sales_goal")
    private val DAILY_GOAL_KEY = doublePreferencesKey("daily_goal")

    // 👇 NOVAS CHAVES PARA O PERFIL V2
    private val BUSINESS_NAME_KEY = stringPreferencesKey("business_name")
    private val BUSINESS_TYPE_KEY = stringPreferencesKey("business_type") // (Inclui "Outro: [tipo]")
    private val BUSINESS_PRODUCTS_KEY = stringSetPreferencesKey("business_products") // (Inclui "Outro: [produto]")

    // --- Funções de Metas ---
    suspend fun setSalesGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[SALES_GOAL_KEY] = goal
        }
    }

    fun getSalesGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[SALES_GOAL_KEY] ?: 0.0
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

    /**
     * Salva todos os dados do perfil de uma vez.
     */
    suspend fun saveUserProfile(
        name: String,
        type: String,      // O tipo final (ex: "Restaurante" ou "Outro: Padaria")
        products: Set<String> // O set final (ex: {"Alimentos", "Outro: Pão Francês"})
    ) {
        dataStore.edit { preferences ->
            preferences[BUSINESS_NAME_KEY] = name
            preferences[BUSINESS_TYPE_KEY] = type
            preferences[BUSINESS_PRODUCTS_KEY] = products
        }
    }

    /**
     * Lê o nome do negócio.
     */
    fun getBusinessName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUSINESS_NAME_KEY] ?: ""
        }
    }

    /**
     * Lê o tipo do negócio.
     */
    fun getBusinessType(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BUSINESS_TYPE_KEY] ?: ""
        }
    }

    /**
     * Lê a lista (Set) de produtos salvos.
     */
    fun getBusinessProducts(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[BUSINESS_PRODUCTS_KEY] ?: emptySet()
        }
    }

    // TODO: Adicionar lógica para salvar 'onboarding_viewed' e 'is_logged_in'
}