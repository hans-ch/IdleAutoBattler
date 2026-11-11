package com.example.idleautobattler.Shop

enum class TipoUpgrade{
    DANO, VIDA_MAXIMA
}

data class Upgrade(
    val id: Int,
    val titulo: String,
    val nivelAtual: Int,
    val precoAtual: Int,
    val tipo: TipoUpgrade
)
