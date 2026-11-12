package com.example.idleautobattler.telas.combate

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressoDao {
    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    fun obterProgressoFlow(): Flow<ProgressoEntity?>

    @Upsert
    suspend fun salvarProgresso(progresso: ProgressoEntity)
}
