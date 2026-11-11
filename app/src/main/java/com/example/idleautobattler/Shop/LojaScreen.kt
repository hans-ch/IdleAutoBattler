package com.example.idleautobattler.Shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.idleautobattler.combate.GameState

@Composable
fun LojaScreen(){

    var gameState by remember { mutableStateOf(GameState(10)) }

    var upgrades by remember {mutableStateOf(
        listOf(
            Upgrade(1, "Aumentat Dano", 0, 10, TipoUpgrade.DANO),
            Upgrade(2, "Aumentar Vida Maxima", 0, 20, TipoUpgrade.VIDA_MAXIMA)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "LOJA", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Ouro: ${gameState.ouro}")

        Spacer(modifier = Modifier.height(16.dp))

        upgrades.forEachIndexed {index, upgrade ->
            ItemDaLoja(
                upgrade = upgrade,
                onComprarClick = {

                    val resultado  = comprarUpgrade(gameState, upgrade)
                    gameState = resultado.first

                    upgrades = upgrades.toMutableList().also {it[index] = resultado.second}

                }
            )
        }

    }

}

