package com.Kenji.pagadvisor.domain.usecase

import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse
import com.Kenji.pagadvisor.domain.repository.PagAdvisorRepository

class GetAiAnalysisUseCase(private val repository: PagAdvisorRepository) {
    suspend operator fun invoke(request: AnalysisRequest): AnalysisResponse {
        return repository.getAnalysis(request)
    }
}