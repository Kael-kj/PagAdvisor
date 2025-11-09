package com.Kenji.pagadvisor.data.remote

import com.Kenji.pagadvisor.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Isso nos permite ver as chamadas de API no Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val longTimeoutHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS) // Aumenta o tempo de conexão
        .readTimeout(60, TimeUnit.SECONDS)    // Aumenta o tempo de leitura (o mais importante)
        .writeTimeout(60, TimeUnit.SECONDS)   // Aumenta o tempo de escrita
        .build()

    // Criação do objeto Retrofit
    val instance: N8nApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(longTimeoutHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(N8nApiService::class.java)
    }
}