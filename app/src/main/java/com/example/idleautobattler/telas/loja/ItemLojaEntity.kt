package com.example.idleautobattler.telas.loja

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itens_loja")
data class ItemLojaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val nivelAtual: Int,
    val precoAtual: Int,
    val tipo: String,
    val aumentoDano: Int = 5
)
