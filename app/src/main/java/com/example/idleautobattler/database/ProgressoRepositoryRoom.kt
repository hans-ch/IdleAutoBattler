package com.example.idleautobattler.database

import com.example.idleautobattler.combate.GameState
import com.example.idleautobattler.combate.ProgressoRepository

/** Adaptador Room para a interface ja usada pelo ViewModel de combate. */
class ProgressoRepositoryRoom(
    private val dao: ProgressoDao,
) : ProgressoRepository {

    override suspend fun carregar(): GameState? = dao.carregar()?.toGameState()

    override suspend fun salvar(estado: GameState) {
        dao.salvar(estado.toProgresso())
    }
}

private fun Progresso.toGameState() = GameState(
    ouro = ouro,
    dano = dano,
    vidaAtual = vidaAtual,
    vidaMaxima = vidaMaxima,
    nivelMonstro = nivelMonstro,
    precoDano = precoDano,
)

private fun GameState.toProgresso() = Progresso(
    ouro = ouro,
    dano = dano,
    vidaAtual = vidaAtual,
    vidaMaxima = vidaMaxima,
    nivelMonstro = nivelMonstro,
    precoDano = precoDano,
)
