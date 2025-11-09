package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) que representa a resposta recebida da API de análise.
 *
 * @property aiReply A resposta em texto gerada pela IA, que será exibida ao usuário.
 */
data class AnalysisResponse(

    @SerializedName("aiReply")
    val aiReply: String

)
