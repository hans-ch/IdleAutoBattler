package com.example.idleautobattler.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.idleautobattler.telas.combate.CombateViewModel
import com.example.idleautobattler.telas.combate.ProgressoDao
import com.example.idleautobattler.telas.combate.ProgressoEntity
import com.example.idleautobattler.telas.loja.ItemLojaDao
import com.example.idleautobattler.telas.loja.ItemLojaEntity
import com.example.idleautobattler.telas.loja.LojaViewModel
import com.example.idleautobattler.telas.skins.SkinDao
import com.example.idleautobattler.telas.skins.SkinEntity
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

class AppViewModelFactory(
    private val repository: JogoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CombateViewModel::class.java) -> {
                CombateViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LojaViewModel::class.java) -> {
                LojaViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
    }
}
