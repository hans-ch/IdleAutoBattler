package com.example.idleautobattler.telas.loja

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "itens_loja")
data class ItemLojaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val nivelAtual: Int,
    val precoAtual: Int,
    val tipo: String, // "DANO", "VIDA_MAXIMA"
    val aumentoDano: Int = 5
)
