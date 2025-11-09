package com.Kenji.pagadvisor.data.remote

import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface Retrofit que define os endpoints da API do n8n.
 *
 * O Retrofit usa esta interface para gerar o código necessário para fazer as
 * chamadas de rede para o seu webhook, mapeando os métodos para os endpoints da API.
 */
interface N8nApiService {

    /**
     * Envia os dados para a análise da IA através de uma requisição POST para o webhook.
     *
     * @param request O objeto [AnalysisRequest] que será enviado como corpo (body) da requisição em formato JSON.
     * @return Um objeto [AnalysisResponse] contendo a resposta da IA.
     */
    @POST("webhook/21ceede0-ea6f-4ae5-a0bc-e4fefcf292c3") // Caminho do endpoint específico do webhook no n8n.
    suspend fun getAnalysis(
        @Body request: AnalysisRequest
    ): AnalysisResponse
}