package com.example.idleautobattler.telas.perfil

import androidx.room.Entity
import androidx.room.PrimaryKey

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
