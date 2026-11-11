package com.example.idleautobattler.data.repository

import com.example.idleautobattler.data.remote.ApiService
import com.example.idleautobattler.data.remote.BonusServidorDto
import com.example.idleautobattler.telas.combate.ProgressoDao
import com.example.idleautobattler.telas.combate.ProgressoEntity
import com.example.idleautobattler.telas.loja.ItemLojaDao
import com.example.idleautobattler.telas.loja.ItemLojaEntity
import com.example.idleautobattler.telas.perfil.SkinDao
import com.example.idleautobattler.telas.perfil.SkinEntity
import kotlinx.coroutines.flow.Flow

class JogoRepositoryImpl(
    private val progressoDao: ProgressoDao,
    private val itemLojaDao: ItemLojaDao,
    private val skinDao: SkinDao,
    private val apiService: ApiService
) : JogoRepository {

    override fun obterProgressoFlow(): Flow<ProgressoEntity?> = progressoDao.obterProgressoFlow()

    override fun obterItensLojaFlow(): Flow<List<ItemLojaEntity>> = itemLojaDao.obterItensLojaFlow()

    override fun obterSkinsFlow(): Flow<List<SkinEntity>> = skinDao.obterSkinsFlow()

    override suspend fun salvarProgresso(progresso: ProgressoEntity): Result<Unit> = try {
        progressoDao.salvarProgresso(progresso)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun adicionarItemLoja(item: ItemLojaEntity): Result<Unit> = try {
        itemLojaDao.inserirItem(item)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun atualizarItemLoja(item: ItemLojaEntity): Result<Unit> = try {
        itemLojaDao.atualizarItem(item)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deletarItemLoja(item: ItemLojaEntity): Result<Unit> = try {
        itemLojaDao.deletarItem(item)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun buscarBonusServidor(): Result<BonusServidorDto> = try {
        val dto = apiService.obterBonusServidor()
        Result.success(dto)
    } catch (_: Exception) {
        Result.success(BonusServidorDto(multiplicadorOuro = 1.0, eventoAtivo = "Modo Offline", servidorOnline = false))
    }
}
