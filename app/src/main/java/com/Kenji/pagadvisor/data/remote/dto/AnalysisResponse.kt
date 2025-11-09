package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnalysisResponse(

    @SerializedName("aiReply")
    val aiReply: String

)