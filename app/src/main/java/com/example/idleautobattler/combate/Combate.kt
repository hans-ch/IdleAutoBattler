package com.example.idleautobattler.combate

import GameState

fun aplicarTick(estado: GameState): GameState {
    val novaVida = estado.vidaAtual - estado.dano

    return if (novaVida > 0) {
        estado.copy(vidaAtual = novaVida)
    } else {
        val novaVidaMaxima = estado.vidaMaxima + Balanceamento.INCREMENTO_VIDA_POR_NIVEL
        estado.copy(
            ouro = estado.ouro + Balanceamento.OURO_POR_NIVEL * estado.nivelMonstro,
            nivelMonstro = estado.nivelMonstro + 1,
            vidaMaxima = novaVidaMaxima,
            vidaAtual = novaVidaMaxima
        )
    }
}

fun proximoPrecoDano(precoAtual: Int): Int = precoAtual * 2