package com.example.idleautobattler.data.repository

import com.example.idleautobattler.data.remote.BonusServidorDto
import com.example.idleautobattler.telas.combate.ProgressoEntity
import com.example.idleautobattler.telas.loja.ItemLojaEntity
import com.example.idleautobattler.telas.perfil.SkinEntity
import kotlinx.coroutines.flow.Flow

interface JogoRepository {
    fun obterProgressoFlow(): Flow<ProgressoEntity?>
    fun obterItensLojaFlow(): Flow<List<ItemLojaEntity>>
    fun obterSkinsFlow(): Flow<List<SkinEntity>>

    suspend fun salvarProgresso(progresso: ProgressoEntity): Result<Unit>
    suspend fun adicionarItemLoja(item: ItemLojaEntity): Result<Unit>
    suspend fun atualizarItemLoja(item: ItemLojaEntity): Result<Unit>
    suspend fun deletarItemLoja(item: ItemLojaEntity): Result<Unit>

    suspend fun buscarBonusServidor(): Result<BonusServidorDto>
}
