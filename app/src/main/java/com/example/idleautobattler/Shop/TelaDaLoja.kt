package com.example.idleautobattler.Shop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemDaLoja(
    upgrade: Upgrade,
    // Essa é a mágica: você deixa o clique "aberto" para o Dev 4 preencher depois
    onComprarClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = upgrade.titulo, style = MaterialTheme.typography.titleMedium)
                Text(text = "Nível: ${upgrade.nivelAtual}")
            }

            Button(onClick = onComprarClick) {
                Text(text = "Comprar (${upgrade.precoAtual} Ouro)")
            }
        }
    }
}