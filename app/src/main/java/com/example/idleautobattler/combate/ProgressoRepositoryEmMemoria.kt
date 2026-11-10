package com.example.idleautobattler.combate

class ProgressoRepositoryEmMemoria(
    private var estadoSalvo: GameState? = null
) : ProgressoRepository {

    val salvamentos = mutableListOf<GameState>()

    override suspend fun carregar(): GameState? = estadoSalvo

    override suspend fun salvar(estado: GameState) {
        estadoSalvo = estado
        salvamentos += estado
    }
}