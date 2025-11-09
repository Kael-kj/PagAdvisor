package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnalysisRequest(
    @SerializedName("userQuery")
    val userQuery: String,
    @SerializedName("salesGoal")
    val salesGoal: Double,
    @SerializedName("weeklySales")
    val weeklySales: List<DailySale>,
    @SerializedName("businessType")
    val businessType: String,
    @SerializedName("businessProducts")
    val businessProducts: List<String>,
    @SerializedName("dailyGoal")
    val dailyGoal: Double,
    @SerializedName("context")
    val context: String? = null
)