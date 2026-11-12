package com.example.idleautobattler.telas.perfil

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
