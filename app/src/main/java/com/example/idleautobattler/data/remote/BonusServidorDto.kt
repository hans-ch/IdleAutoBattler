package com.example.idleautobattler.data.remote

import com.google.gson.annotations.SerializedName

data class BonusServidorDto(
    @SerializedName("multiplicador_ouro") val multiplicadorOuro: Double = 1.5,
    @SerializedName("evento_ativo") val eventoAtivo: String = "Bônus de Fim de Semana (+50% Ouro)",
    @SerializedName("servidor_online") val servidorOnline: Boolean = true
)
