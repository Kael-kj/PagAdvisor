package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta classe representa uma única linha no JSON de vendas que enviamos
data class DailySale(
    @SerializedName("dayOfWeek") // Garante que o nome no JSON seja este
    val dayOfWeek: String,
    @SerializedName("totalSold")
    val totalSold: Double
)