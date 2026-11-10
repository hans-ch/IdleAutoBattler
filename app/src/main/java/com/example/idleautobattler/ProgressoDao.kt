package com.example.idleautobattler.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProgressoDao {
    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    suspend fun carregar(): Progresso?

    @Upsert
    suspend fun salvar(progresso: Progresso)
}
