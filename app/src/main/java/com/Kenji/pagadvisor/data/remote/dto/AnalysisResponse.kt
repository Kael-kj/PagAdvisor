package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta é a classe que RECEBEMOS de volta do n8n
data class AnalysisResponse(
    @SerializedName("aiReply")
    val aiReply: String
)