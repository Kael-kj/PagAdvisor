package com.Kenji.pagadvisor.data.repository

import com.Kenji.pagadvisor.data.remote.N8nApiService
import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse
import com.Kenji.pagadvisor.domain.repository.PagAdvisorRepository

class PagAdvisorRepositoryImpl(
    private val api: N8nApiService // Vamos injetar o RetrofitClient.instance aqui
) : PagAdvisorRepository {

    override suspend fun getAnalysis(request: AnalysisRequest): AnalysisResponse {
        return api.getAnalysis(request)
    }
}