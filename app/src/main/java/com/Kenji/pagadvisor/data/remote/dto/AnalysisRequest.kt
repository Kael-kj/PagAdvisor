package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) que representa o corpo (body) da requisição enviada para a API de análise.
 *
 * Este objeto agrega todos os dados que a IA precisa para contextualizar e responder à pergunta do usuário.
 * A anotação `@SerializedName` garante que o nome do campo no JSON corresponda ao esperado pela API n8n,
 * mesmo que o nome da variável no Kotlin seja diferente.
 */
data class AnalysisRequest(
    /** A pergunta ou comando específico enviado pelo usuário. */
    @SerializedName("userQuery")
    val userQuery: String,

    /** A meta de vendas semanal definida pelo usuário. */
    @SerializedName("salesGoal")
    val salesGoal: Double,

    /** Uma lista com os dados de vendas diárias da semana. */
    @SerializedName("weeklySales")
    val weeklySales: List<DailySale>,

    /** O tipo de negócio do usuário (ex: "Restaurante", "Loja de Roupas"). */
    @SerializedName("businessType")
    val businessType: String,

    /** Uma lista dos principais produtos ou serviços que o usuário vende. */
    @SerializedName("businessProducts")
    val businessProducts: List<String>,

    /** A meta de vendas diária definida pelo usuário. */
    @SerializedName("dailyGoal")
    val dailyGoal: Double,

    /** 
     * O texto da última resposta da IA. Usado para manter o contexto em conversas
     * com múltiplas interações (ex: quando o usuário responde a uma pergunta da IA).
     * É opcional (pode ser nulo).
     */
    @SerializedName("context")
    val context: String? = null
)
