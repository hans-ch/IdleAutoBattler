package com.example.idleautobattler.telas.perfil

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "skins")
data class SkinEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val descricao: String,
    val raritade: String,
    val iconeEmoji: String,
    val precoOuro: Int,
    val desbloqueada: Boolean = false,
    val equipada: Boolean = false
)
