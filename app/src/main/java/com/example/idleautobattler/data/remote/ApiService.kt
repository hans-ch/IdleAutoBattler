package com.example.idleautobattler.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET("bonus_evento.json")
    suspend fun obterBonusServidor(): BonusServidorDto
}
