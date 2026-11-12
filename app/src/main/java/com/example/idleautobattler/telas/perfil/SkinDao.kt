package com.example.idleautobattler.telas.perfil

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import com.example.idleautobattler.data.SkinEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface SkinDao {
    @Query("SELECT * FROM skins ORDER BY nome")
    fun obterSkinsFlow(): Flow<List<SkinEntity>>

    @Query("SELECT * FROM skins WHERE equipada = 1 LIMIT 1")
    fun obterSkinEquipadaFlow(): Flow<SkinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSkin(skin: SkinEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSkins(skins: List<SkinEntity>)

    @Update
    suspend fun atualizarSkin(skin: SkinEntity)

    @Query("SELECT COUNT(*) FROM skins")
    suspend fun contarSkins(): Int

    @Query("UPDATE skins SET desbloqueada = 1 WHERE id = :skinId")
    suspend fun desbloquearSkin(skinId: Int)

    @Query("UPDATE skins SET equipada = 0")
    suspend fun desequiparTodas()

    @Query(
        """
        UPDATE skins
        SET equipada = 1
        WHERE id = :skinId AND desbloqueada = 1
        """
    )
    suspend fun marcarComoEquipada(skinId: Int)

    @Transaction
    suspend fun equiparSkin(skinId: Int) {
        desequiparTodas()
        marcarComoEquipada(skinId)
    }
}