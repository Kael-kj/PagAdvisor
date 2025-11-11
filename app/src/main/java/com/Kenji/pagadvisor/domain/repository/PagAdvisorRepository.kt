package com.Kenji.pagadvisor.domain.repository

import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse

/**
 * Interface que define o contrato para o repositório de análise da IA (PagAdvisor).
 *
 * Na Clean Architecture, a camada de domínio define as interfaces dos repositórios,
 * e a camada de dados (data) fornece a implementação concreta. Isso desacopla a lógica
 * de negócio dos detalhes de implementação da fonte de dados.
 */
interface PagAdvisorRepository {

    /**
     * Contrato para o método que busca a análise da IA.
     *
     * @param request O objeto [AnalysisRequest] com os dados a serem analisados.
     * @return Um [AnalysisResponse] contendo a resposta da IA.
     */
    suspend fun getAnalysis(request: AnalysisRequest): List<AnalysisResponse>
}