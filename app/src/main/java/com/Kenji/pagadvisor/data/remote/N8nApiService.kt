package com.Kenji.pagadvisor.data.remote

import com.Kenji.pagadvisor.data.remote.dto.AnalysisRequest
import com.Kenji.pagadvisor.data.remote.dto.AnalysisResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface N8nApiService {

    @POST("webhook/21ceede0-ea6f-4ae5-a0bc-e4fefcf292c3")
    suspend fun getAnalysis(
        @Body request: AnalysisRequest
    ): AnalysisResponse
}