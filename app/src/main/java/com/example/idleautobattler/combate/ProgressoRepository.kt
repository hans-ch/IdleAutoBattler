package com.example.idleautobattler.combate

import GameState

interface ProgressoRepository {
    suspend fun carregar(): GameState?
    suspend fun salvar(estado: GameState)
}