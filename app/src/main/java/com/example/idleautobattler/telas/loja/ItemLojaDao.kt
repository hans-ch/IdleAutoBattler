package com.example.idleautobattler.telas.loja

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemLojaDao {
    @Query("SELECT * FROM itens_loja")
    fun obterItensLojaFlow(): Flow<List<ItemLojaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirItem(item: ItemLojaEntity)

    @Update
    suspend fun atualizarItem(item: ItemLojaEntity)

    @Delete
    suspend fun deletarItem(item: ItemLojaEntity)
}
