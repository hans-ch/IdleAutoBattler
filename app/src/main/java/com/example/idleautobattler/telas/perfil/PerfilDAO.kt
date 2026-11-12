package com.example.idleautobattler.telas.perfil

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    @Query("SELECT * FROM perfil WHERE id = 1")
    fun observarPerfil(): Flow<PerfilEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarPerfil(perfil: PerfilEntity)

    @Query("UPDATE perfil SET nome = :nome WHERE id = 1")
    suspend fun atualizarNome(nome: String)

    @Query(
        """
        UPDATE perfil
        SET ouro = ouro - :preco
        WHERE id = 1 AND ouro >= :preco
        """
    )
    suspend fun descontarOuroSePossivel(preco: Int): Int
}