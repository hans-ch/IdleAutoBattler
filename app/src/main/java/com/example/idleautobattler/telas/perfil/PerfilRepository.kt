package com.example.idleautobattler.telas.perfil

import androidx.room3.immediateTransaction
import androidx.room3.useWriterConnection
import com.example.idleautobattler.data.AppDatabase
import com.example.idleautobattler.data.ProgressoEntity
import com.example.idleautobattler.data.SkinEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PerfilRepository(
    private val database: AppDatabase
) {
    private val progressoDao = database.progressoDao()
    private val skinDao = database.skinDao()

    val progresso: Flow<ProgressoEntity?> =
        progressoDao.obterProgressoFlow()

    val skins: Flow<List<SkinEntity>> =
        skinDao.obterSkinsFlow()

    val skinEquipada: Flow<SkinEntity?> =
        skinDao.obterSkinEquipadaFlow()

    suspend fun inicializar(nomeInicial: String) {
        if (progressoDao.obterProgressoFlow().first() == null) {
            progressoDao.salvarProgresso(
                ProgressoEntity(
                    nomeJogador = nomeInicial.trim().ifBlank { "Guerreiro" }
                )
            )
        }

        if (skinDao.contarSkins() == 0) {
            skinDao.inserirSkins(
                listOf(
                    SkinEntity(
                        nome = "Herói Padrão",
                        descricao = "A aparência clássica do herói.",
                        raritade = "Comum",
                        iconeEmoji = "🗡️",
                        precoOuro = 0,
                        desbloqueada = true,
                        equipada = true
                    ),
                    SkinEntity(
                        nome = "Mago de Fogo",
                        descricao = "Uma aparência rara envolta em chamas.",
                        raritade = "Rara",
                        iconeEmoji = "🧙",
                        precoOuro = 300
                    ),
                    SkinEntity(
                        nome = "Ninja das Sombras",
                        descricao = "Uma skin épica das sombras.",
                        raritade = "Épica",
                        iconeEmoji = "🥷",
                        precoOuro = 750
                    ),
                    SkinEntity(
                        nome = "Cyborg RPG",
                        descricao = "Uma aparência tecnológica.",
                        raritade = "Épica",
                        iconeEmoji = "🤖",
                        precoOuro = 1500
                    ),
                    SkinEntity(
                        nome = "Matador de Dragões",
                        descricao = "Uma skin lendária de caçador.",
                        raritade = "Lendária",
                        iconeEmoji = "🐲",
                        precoOuro = 3000
                    ),
                    SkinEntity(
                        nome = "Rei Supremo",
                        descricao = "A aparência de um rei lendário.",
                        raritade = "Lendária",
                        iconeEmoji = "👑",
                        precoOuro = 5000
                    )
                )
            )
        }
    }

    suspend fun atualizarNome(nome: String) {
        val nomeValidado = nome.trim()
        if (nomeValidado.isNotEmpty()) {
            progressoDao.atualizarNome(nomeValidado)
        }
    }

    suspend fun comprarSkin(skin: SkinEntity): Boolean {
        if (skin.desbloqueada) return true

        return database.useWriterConnection { connection ->
            connection.immediateTransaction {
                val linhasAtualizadas =
                    progressoDao.descontarOuroSeSuficiente(skin.precoOuro)

                if (linhasAtualizadas == 0) {
                    false
                } else {
                    skinDao.desbloquearSkin(skin.id)
                    true
                }
            }
        }
    }
    suspend fun equiparSkin(skin: SkinEntity): Boolean {
        if (!skin.desbloqueada) return false

        return database.useWriterConnection { connection ->
            connection.immediateTransaction {
                skinDao.equiparSkin(skin.id)

                val progressoAtual =
                    progressoDao.obterProgressoFlow().first()
                        ?: return@immediateTransaction false

                progressoDao.salvarProgresso(
                    progressoAtual.copy(skinEquipada = skin.nome)
                )

                true
            }
        }
    }
}