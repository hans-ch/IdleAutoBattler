package com.example.idleautobattler.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface ProgressoDao {
    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    suspend fun carregar(): Progresso?

    @Upsert
    suspend fun salvar(progresso: Progresso)
}
