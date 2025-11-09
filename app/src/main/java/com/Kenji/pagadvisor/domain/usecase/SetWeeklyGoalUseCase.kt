package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository

/**
 * Caso de uso para definir a meta de vendas semanal.
 *
 * Esta classe encapsula a lógica para salvar a meta semanal no repositório.
 *
 * @property repository O repositório de vendas onde a meta será salva.
 */
class SetWeeklyGoalUseCase(private val repository: SalesRepository) {

    /**
     * Executa o caso de uso para salvar a nova meta semanal.
     *
     * @param goal O valor da nova meta de vendas semanal.
     */
    suspend operator fun invoke(goal: Double) {
        repository.setWeeklyGoal(goal)
    }
}