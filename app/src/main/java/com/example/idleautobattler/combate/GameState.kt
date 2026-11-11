package com.example.idleautobattler.combate

data class GameState(
    val ouro: Int = 0,
    val dano: Int = 1,
    val vidaAtual: Int = 10,
    val vidaMaxima: Int = 10,
    val nivelMonstro: Int = 1,
    val precoDano: Int = 10
)