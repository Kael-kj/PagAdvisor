package com.Kenji.pagadvisor.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensão para o [Context] que cria uma instância singleton do [preferencesDataStore].
 * O nome "user_preferences" define o nome do arquivo onde os dados serão salvos localmente.
 */
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

/**
 * Repositório para gerenciar o armazenamento de preferências e dados do usuário localmente.
 *
 * Esta classe utiliza o Jetpack DataStore para persistir dados de forma assíncrona e segura,
 * como metas de vendas e informações do perfil do negócio.
 *
 * @param context O contexto da aplicação, usado para inicializar o DataStore.
 */
class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    /**
     * Objeto privado que contém todas as chaves (Keys) usadas para salvar e recuperar dados do DataStore.
     * Agrupar as chaves evita erros de digitação e centraliza a definição dos tipos de dados.
     */
    private object PreferencesKeys {
        val WEEKLY_GOAL_KEY = doublePreferencesKey("sales_goal")
        val DAILY_GOAL_KEY = doublePreferencesKey("daily_goal")
        val MONTHLY_GOAL_KEY = doublePreferencesKey("monthly_goal")
        val ANNUAL_GOAL_KEY = doublePreferencesKey("annual_goal")

        val BUSINESS_NAME_KEY = stringPreferencesKey("business_name")
        val BUSINESS_TYPE_KEY = stringPreferencesKey("business_type")
        val BUSINESS_PRODUCTS_KEY = stringSetPreferencesKey("business_products")
    }

    // --- Funções de Metas ---

    /** Salva a meta de vendas semanal no DataStore. */
    suspend fun setWeeklyGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WEEKLY_GOAL_KEY] = goal
        }
    }

    /** Recupera a meta de vendas semanal como um [Flow]. Retorna 0.0 se não houver valor salvo. */
    fun getWeeklyGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.WEEKLY_GOAL_KEY] ?: 0.0
        }
    }

    /** Salva a meta de vendas diária no DataStore. */
    suspend fun setDailyGoal(dailyGoal: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_GOAL_KEY] = dailyGoal
        }
    }

    /** Recupera a meta de vendas diária como um [Flow]. Retorna 0.0 se não houver valor salvo. */
    fun getDailyGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.DAILY_GOAL_KEY] ?: 0.0
        }
    }

    /** Salva a meta de vendas mensal no DataStore. */
    suspend fun setMonthlyGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MONTHLY_GOAL_KEY] = goal
        }
    }

    /** Recupera a meta de vendas mensal como um [Flow]. Retorna 0.0 se não houver valor salvo. */
    fun getMonthlyGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.MONTHLY_GOAL_KEY] ?: 0.0
        }
    }

    /** Salva a meta de vendas anual no DataStore. */
    suspend fun setAnnualGoal(goal: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANNUAL_GOAL_KEY] = goal
        }
    }

    /** Recupera a meta de vendas anual como um [Flow]. Retorna 0.0 se não houver valor salvo. */
    fun getAnnualGoal(): Flow<Double> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ANNUAL_GOAL_KEY] ?: 0.0
        }
    }

    // --- Funções de Perfil ---

    /** Salva as informações de perfil do negócio (nome, tipo e produtos) no DataStore. */
    suspend fun saveUserProfile(name: String, type: String, products: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BUSINESS_NAME_KEY] = name
            preferences[PreferencesKeys.BUSINESS_TYPE_KEY] = type
            preferences[PreferencesKeys.BUSINESS_PRODUCTS_KEY] = products
        }
    }

    /** Recupera o nome do negócio como um [Flow]. Retorna uma string vazia se não houver valor salvo. */
    fun getBusinessName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.BUSINESS_NAME_KEY] ?: ""
        }
    }

    /** Recupera o tipo do negócio como um [Flow]. Retorna uma string vazia se não houver valor salvo. */
    fun getBusinessType(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.BUSINESS_TYPE_KEY] ?: ""
        }
    }

    /** Recupera o conjunto de produtos do negócio como um [Flow]. Retorna um conjunto vazio se não houver valor salvo. */
    fun getBusinessProducts(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.BUSINESS_PRODUCTS_KEY] ?: emptySet()
        }
    }

    /**
     * Limpa todos os dados salvos no DataStore.
     * Utilizado para a funcionalidade de logout.
     */
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
