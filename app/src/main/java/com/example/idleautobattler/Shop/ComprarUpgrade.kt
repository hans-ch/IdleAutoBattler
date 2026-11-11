package com.example.idleautobattler.Shop

import GameState

fun comprarUpgrade(
    gameState: GameState,
    upgrade: Upgrade
): Pair<GameState, Upgrade> {

    if(gameState.ouro < upgrade.precoAtual){
        return Pair(gameState, upgrade)
    }
    else {
        // desconta o ouro
        var novoGameState =
            gameState.copy(ouro = gameState.ouro - upgrade.precoAtual) //cria novo gameState

        novoGameState = when (upgrade.tipo){

            TipoUpgrade.DANO -> novoGameState.copy(dano = novoGameState.dano + 1)
            TipoUpgrade.VIDA_MAXIMA -> novoGameState.copy(vidaMaxima = novoGameState.vidaMaxima + 5)
        }



        val novoUpgrade =
            upgrade.copy(nivelAtual = upgrade.nivelAtual + 1, precoAtual = upgrade.precoAtual + 10)

        return Pair(novoGameState, novoUpgrade)
    }

}
