package com.Kenji.pagadvisor.data.repository

import com.Kenji.pagadvisor.data.remote.N8nApiService
import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse
import com.Kenji.pagadvisor.domain.repository.PagAdvisorRepository

/**
 * Implementação do repositório [PagAdvisorRepository].
 *
 * Esta classe é a implementação concreta da interface do domínio e é responsável por
 * se comunicar com a API externa (n8n) para obter a análise da IA.
 *
 * @property api A instância do serviço Retrofit [N8nApiService] usada para fazer as chamadas de rede.
 */
class PagAdvisorRepositoryImpl(
    private val apiService: N8nApiService
) : PagAdvisorRepository {
    override suspend fun getAnalysis(request: AnalysisRequest): List<AnalysisResponse> { // <-- MUDE AQUI!
        return apiService.getAnalysis(request)
    }
}