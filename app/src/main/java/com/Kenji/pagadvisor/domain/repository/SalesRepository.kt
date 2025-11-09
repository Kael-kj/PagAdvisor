package com.Kenji.pagadvisor.domain.repository

import com.Kenji.pagadvisor.data.remote.dto.DailySale
import com.Kenji.pagadvisor.domain.usecase.CustomerStats
import kotlinx.coroutines.flow.Flow

/**
 * Interface que define o contrato para o repositório de dados de vendas, metas e perfil do usuário.
 *
 * Este contrato é usado pela camada de domínio (Use Cases) para solicitar dados, abstraindo
 * a origem desses dados (se vêm de uma API, banco de dados local, DataStore ou se são mocados).
 */
interface SalesRepository {

    /** Retorna uma lista de dados de vendas semanais (atualmente mocados para desenvolvimento). */
    suspend fun getMockedSales(): List<DailySale>

    /** Salva as informações do perfil do negócio do usuário. */
    suspend fun saveUserProfile(name: String, type: String, products: Set<String>)

    /** Recupera o nome do negócio do usuário como um Flow. */
    fun getBusinessName(): Flow<String>

    /** Recupera o tipo de negócio do usuário como um Flow. */
    fun getBusinessType(): Flow<String>

    /** Recupera os produtos/serviços do negócio do usuário como um Flow. */
    fun getBusinessProducts(): Flow<Set<String>>

    /** Salva a meta diária de vendas. */
    suspend fun setDailyGoal(dailyGoal: Double)

    /** Recupera a meta diária de vendas como um Flow. */
    fun getDailyGoal(): Flow<Double>

    /** Salva a meta semanal de vendas. */
    suspend fun setWeeklyGoal(goal: Double)

    /** Recupera a meta semanal de vendas como um Flow. */
    fun getWeeklyGoal(): Flow<Double>

    /** Salva a meta mensal de vendas. */
    suspend fun setMonthlyGoal(goal: Double)

    /** Recupera a meta mensal de vendas como um Flow. */
    fun getMonthlyGoal(): Flow<Double>

    /** Salva a meta anual de vendas. */
    suspend fun setAnnualGoal(goal: Double)

    /** Recupera a meta anual de vendas como um Flow. */
    fun getAnnualGoal(): Flow<Double>

    /** Recupera o valor total a receber (contas a receber). */
    suspend fun getReceivables(): Double

    /** Recupera as estatísticas de clientes (total e novos na semana). */
    suspend fun getCustomerStats(): CustomerStats

    /** Limpa os dados do usuário para efetuar o logout. */
    suspend fun logout()
}