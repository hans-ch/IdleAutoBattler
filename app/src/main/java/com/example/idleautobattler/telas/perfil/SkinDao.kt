package com.example.idleautobattler.telas.perfil

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SkinDao {
    @Query("SELECT * FROM skins")
    fun obterSkinsFlow(): Flow<List<SkinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSkin(skin: SkinEntity)

    @Update
    suspend fun atualizarSkin(skin: SkinEntity)

    @Delete
    suspend fun deletarSkin(skin: SkinEntity)
}
