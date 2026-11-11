package com.example.idleautobattler.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/** Unica linha com o progresso persistente do jogador. */
@Entity(tableName = "progresso")
data class Progresso(
    @PrimaryKey
    val id: Int = ID_UNICO,
    val ouro: Int,
    val dano: Int,
    val vidaAtual: Int = 10,
    val vidaMaxima: Int = 10,
    val nivelMonstro: Int = 1,
    val precoDano: Int = 10,
) {
    companion object {
        const val ID_UNICO = 1
    }
}
