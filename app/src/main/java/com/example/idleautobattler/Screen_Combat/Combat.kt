package com.example.idleautobattler.Screen_Combat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TelaBatalhaAuto() {
    // Estados do jogo
    var nomeMonstro by remember { mutableStateOf("Goblin Selvagem") }
    var vidaMonstroAtual by remember { mutableFloatStateOf(1.0f) } // 1.0 = 100%
    var ouroJogador by remember { mutableIntStateOf(100) }
    var emBatalha by remember { mutableStateOf(false) }
    var statusTexto by remember { mutableStateOf("Toque em 'Iniciar Batalha' para começar.") }

    // Loop de combate automático utilizando Corrotinas
    LaunchedEffect(emBatalha) {
        if (emBatalha) {
            statusTexto = "Batalha emandamento..."
            while (emBatalha && vidaMonstroAtual > 0f) {
                delay(800) // Intervalo de cada golpe (0.8 segundos)

                // Reduz a vida do monstro
                vidaMonstroAtual -= 0.2f

                if (vidaMonstroAtual <= 0f) {
                    vidaMonstroAtual = 0f
                    ouroJogador += 25
                    statusTexto = "Monstro derrotado! Ganhou 25 de ouro."
                    delay(1200) // Pausa curta antes de nascer o próximo monstro

                    // Reseta para um novo monstro
                    vidaMonstroAtual = 1.0f
                    nomeMonstro = if (nomeMonstro == "Goblin Selvagem") "Orc Guerreiro" else "Goblin Selvagem"
                    statusTexto = "Um novo $nomeMonstro apareceu!"
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ouro do Jogador (Topo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "🪙 Ouro: $ouroJogador",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Área Central (Inimigo e Jogador)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Inimigo
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Monstro",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = nomeMonstro, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Barra de Vida
                    LinearProgressIndicator(
                        progress = { vidaMonstroAtual },
                        modifier = Modifier
                            .width(220.dp)
                            .height(10.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(vidaMonstroAtual * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(text = "VS", fontSize = 18.sp, style = MaterialTheme.typography.bodyLarge)

                // Jogador
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Herói",
                        modifier = Modifier.size(70.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Herói Auto-Atacante", style = MaterialTheme.typography.titleMedium)
                }

                // Log de Status
                Text(
                    text = statusTexto,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Botão de Controle (Iniciar / Pausar)
            Button(
                onClick = { emBatalha = !emBatalha },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (emBatalha) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (emBatalha) "Pausar Batalha" else "Iniciar Batalha Automática",
                    fontSize = 16.sp
                )
            }
        }
    }
}