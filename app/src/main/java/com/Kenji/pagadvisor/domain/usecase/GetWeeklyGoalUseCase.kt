package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
class GetWeeklyGoalUseCase(private val repository: SalesRepository) {
    operator fun invoke(): Flow<Double> {
        return repository.getWeeklyGoal()
    }
}