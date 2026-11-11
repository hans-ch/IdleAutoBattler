package com.example.idleautobattler.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class BonusServidorDto(
    @SerializedName("multiplicador_ouro") val multiplicadorOuro: Double = 1.5,
    @SerializedName("evento_ativo") val eventoAtivo: String = "Bônus de Fim de Semana (+50% Ouro)",
    @SerializedName("servidor_online") val servidorOnline: Boolean = true
)

interface ApiService {
    @GET("bonus_evento.json")
    suspend fun obterBonusServidor(): BonusServidorDto
}

object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/idleautobattler/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
