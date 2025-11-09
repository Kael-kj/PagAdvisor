package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository
class SetWeeklyGoalUseCase(private val repository: SalesRepository) {
    suspend operator fun invoke(goal: Double) {
        repository.setWeeklyGoal(goal)
    }
}