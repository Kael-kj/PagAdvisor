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
    private val api: N8nApiService
) : PagAdvisorRepository {

    /**
     * Envia os dados de vendas e a pergunta do usuário para o serviço da IA e retorna a análise.
     *
     * @param request O objeto [AnalysisRequest] contendo todos os dados necessários para a análise.
     * @return Um objeto [AnalysisResponse] com a resposta da IA.
     */
    override suspend fun getAnalysis(request: AnalysisRequest): AnalysisResponse {
        return api.getAnalysis(request)
    }
}