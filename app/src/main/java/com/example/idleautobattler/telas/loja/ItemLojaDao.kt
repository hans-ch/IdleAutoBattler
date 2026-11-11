package com.example.idleautobattler.telas.loja

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
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

    @Query("DELETE FROM itens_loja")
    suspend fun limparTodos()
}
