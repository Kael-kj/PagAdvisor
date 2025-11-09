package com.Kenji.pagadvisor.data.repository

import com.Kenji.pagadvisor.data.local.UserPreferencesRepository
import com.Kenji.pagadvisor.data.remote.dto.DailySale
import com.Kenji.pagadvisor.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import com.Kenji.pagadvisor.domain.usecase.CustomerStats

/**
 * Implementação do repositório [SalesRepository].
 *
 * Esta classe é responsável por fornecer dados de vendas, metas e perfil do usuário.
 * Atualmente, ela utiliza uma combinação de dados mocados (fixos no código) para as vendas
 * e o [UserPreferencesRepository] (DataStore) para persistir as informações do perfil e metas.
 *
 * @property preferences O repositório de preferências do usuário para acessar o DataStore.
 */
class SalesRepositoryImpl(
    private val preferences: UserPreferencesRepository
) : SalesRepository {

    /**
     * Retorna uma lista de dados de vendas semanais mocados.
     * Utilizado para fins de desenvolvimento e demonstração.
     */
    override suspend fun getMockedSales(): List<DailySale> {
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

    /** Salva os dados do perfil do usuário (nome, tipo de negócio, produtos) no DataStore. */
    override suspend fun saveUserProfile(name: String, type: String, products: Set<String>) {
        preferences.saveUserProfile(name, type, products)
    }

    /** Recupera o nome do negócio do DataStore. */
    override fun getBusinessName(): Flow<String> {
        return preferences.getBusinessName()
    }

    /** Recupera o tipo do negócio do DataStore. */
    override fun getBusinessType(): Flow<String> {
        return preferences.getBusinessType()
    }

    /** Recupera a lista de produtos/serviços do negócio do DataStore. */
    override fun getBusinessProducts(): Flow<Set<String>> {
        return preferences.getBusinessProducts()
    }

    /** Retorna um valor mocado para contas a receber. */
    override suspend fun getReceivables(): Double {
        return 4300.00
    }

    /** Retorna estatísticas de clientes mocadas. */
    override suspend fun getCustomerStats(): CustomerStats {
        return CustomerStats(
            totalCustomers = 120,
            newCustomersThisWeek = 5
        )
    }

    /** Salva a meta semanal no DataStore. */
    override suspend fun setWeeklyGoal(goal: Double) {
        preferences.setWeeklyGoal(goal)
    }

    /** Salva a meta diária no DataStore. */
    override suspend fun setDailyGoal(dailyGoal: Double) {
        preferences.setDailyGoal(dailyGoal)
    }

    /** Recupera a meta diária do DataStore. */
    override fun getDailyGoal(): Flow<Double> {
        return preferences.getDailyGoal()
    }

    /** Recupera a meta semanal do DataStore. */
    override fun getWeeklyGoal(): Flow<Double> {
        return preferences.getWeeklyGoal()
    }

    /** Salva a meta mensal no DataStore. */
    override suspend fun setMonthlyGoal(goal: Double) {
        preferences.setMonthlyGoal(goal)
    }

    /** Recupera a meta mensal do DataStore. */
    override fun getMonthlyGoal(): Flow<Double> {
        return preferences.getMonthlyGoal()
    }

    /** Salva a meta anual no DataStore. */
    override suspend fun setAnnualGoal(goal: Double) {
        preferences.setAnnualGoal(goal)
    }

    /** Recupera a meta anual do DataStore. */
    override fun getAnnualGoal(): Flow<Double> {
        return preferences.getAnnualGoal()
    }

    /** Realiza o logout do usuário, limpando os dados salvos no DataStore. */
    override suspend fun logout() {
        preferences.logout()
    }
}