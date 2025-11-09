package com.Kenji.pagadvisor.data.remote

import com.Kenji.pagadvisor.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto singleton responsável pela configuração e criação da instância do Retrofit.
 * Utiliza um cliente OkHttp customizado com timeouts estendidos e logging de requisições.
 */
object RetrofitClient {

    /**
     * Interceptor para registrar o corpo (body) das requisições e respostas da API no Logcat.
     * Essencial para depuração de chamadas de rede.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente OkHttp com timeouts mais longos (60 segundos) para requisições, leituras e escritas.
     * Isso evita que chamadas de API mais demoradas (como as da IA) falhem por timeout.
     */
    private val longTimeoutHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS) // Tempo para estabelecer a conexão
        .readTimeout(60, TimeUnit.SECONDS)    // Tempo para ler os dados da resposta
        .writeTimeout(60, TimeUnit.SECONDS)   // Tempo para enviar os dados da requisição
        .build()

    /**
     * A instância pública e preguiçosa (lazy) do serviço da API (N8nApiService).
     * O Retrofit só é inicializado na primeira vez que esta instância é acessada.
     */
    val instance: N8nApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // URL base vinda do arquivo .env através do BuildConfig
            .client(longTimeoutHttpClient) // Usa o cliente OkHttp com timeouts longos
            .addConverterFactory(GsonConverterFactory.create()) // Converte JSON em objetos Kotlin
            .build()
            .create(N8nApiService::class.java)
    }
}
