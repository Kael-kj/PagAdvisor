package com.Kenji.pagadvisor.domain.repository

import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse

interface PagAdvisorRepository {
    suspend fun getAnalysis(request: AnalysisRequest): AnalysisResponse
}