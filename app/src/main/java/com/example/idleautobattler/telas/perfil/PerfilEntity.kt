package com.example.idleautobattler.telas.perfil

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey
    val id: Int = 1,

    val nome: String = "Guerreiro",
    val ouro: Int = 1000
)