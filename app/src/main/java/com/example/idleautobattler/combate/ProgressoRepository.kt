package com.example.idleautobattler.combate

interface ProgressoRepository {
    suspend fun carregar(): GameState?
    suspend fun salvar(estado: GameState)
}