package com.Kenji.pagadvisor.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) que representa os dados de venda de um único dia.
 * Usado na lista [AnalysisRequest.weeklySales] para enviar o histórico de vendas para a IA.
 *
 * @property dayOfWeek O dia da semana correspondente à venda (ex: "Seg", "Ter").
 * @property totalSold O valor total vendido naquele dia.
 */
data class DailySale(
    @SerializedName("dayOfWeek")
    val dayOfWeek: String,

    @SerializedName("totalSold")
    val totalSold: Double
)
