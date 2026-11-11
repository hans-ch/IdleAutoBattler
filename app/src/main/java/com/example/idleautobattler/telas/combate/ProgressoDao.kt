package com.example.idleautobattler.telas.combate

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressoDao {
    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    fun obterProgressoFlow(): Flow<ProgressoEntity?>

    @Query("SELECT * FROM progresso WHERE id = 1 LIMIT 1")
    suspend fun carregarProgresso(): ProgressoEntity?

    @Upsert
    suspend fun salvarProgresso(progresso: ProgressoEntity)
}
