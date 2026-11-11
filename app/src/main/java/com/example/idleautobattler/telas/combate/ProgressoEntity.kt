package com.example.idleautobattler.telas.combate

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "progresso")
data class ProgressoEntity(
    @PrimaryKey val id: Int = 1,
    val ouro: Int = 1250,
    val dano: Int = 15,
    val vidaAtual: Int = 100,
    val vidaMaxima: Int = 100,
    val nivelMonstro: Int = 1,
    val skinEquipada: String = "Herói Padrão"
)
