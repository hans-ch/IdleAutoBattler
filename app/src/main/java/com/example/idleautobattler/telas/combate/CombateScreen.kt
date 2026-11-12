package com.example.idleautobattler.telas.combate

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CombateScreen(
    uiState: CombateUiState,
    onGanharOuro: (Int) -> Unit = {},
    onPerderOuro: (Int) -> Unit = {},
    onGolpeEspecial: () -> Unit = {}
) {
    // Define as cores de fundo com base na fase atual
    val coresFundo = when (uiState.faseAtual % 3) {
        1 -> listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460)) // Caverna de Cristal (Azul)
        2 -> listOf(Color(0xFF1B3B2B), Color(0xFF11261B), Color(0xFF09140E)) // Floresta Sombria (Verde)
        else -> listOf(Color(0xFF3E1A1A), Color(0xFF2E1616), Color(0xFF1A0F0F)) // Castelo Vulcânico (Vermelho/Escuro)
    }

    val escalaHeroi by animateFloatAsState(
        targetValue = if (uiState.animarAtaqueHeroi) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaHeroi"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = coresFundo)
            )
            .padding(16.dp)
    ) {
        // Coluna principal para organizar o topo, o centro e o rodapé perfeitamente
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra superior com Ouro, Fase e DPS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Ouro",
                            tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${uiState.ouro}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // Indicador de Fase Atual
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Fase ${uiState.faseAtual} (${uiState.monstrosDerrotadosNaFase}/${uiState.monstrosNecessariosParaProximaFase})",
                        color = Color(0xFFFFD700),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "DPS: ${uiState.dano}/s",
                        color = Color(0xFF00FFCC),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Área central: Monstro, VS e Herói
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Seção do Monstro
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val emojiMonstro = when (uiState.nomeMonstro) {
                            "Dragão de Cristal" -> "🐉"
                            "Rei Goblin Corrompido" -> "👺"
                            "Esqueleto Guerreiro" -> "💀"
                            "Golem de Pedra" -> "🗿"
                            else -> "👾"
                        }

                        Text(
                            text = emojiMonstro,
                            fontSize = 42.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.nomeMonstro, color = Color.White, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(6.dp))

                    val vidaMonstroTotalMax = uiState.vidaMonstroMax + 50f
                    LinearProgressIndicator(
                        progress = { (uiState.vidaMonstroAtual / vidaMonstroTotalMax).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .width(220.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color(0xFFFF416C),
                        trackColor = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${uiState.vidaMonstroAtual.toInt()} / ${vidaMonstroTotalMax.toInt()}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "⚔️ VS ⚔️",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Seção do Herói
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(escalaHeroi)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Blue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val emojiSkin = when (uiState.skinEquipada) {
                            "Mago de Fogo" -> "🧙‍♂️"
                            "Ninja das Sombras" -> "🥷"
                            "Cyborg RPG" -> "🤖"
                            "Matador de Dragões" -> "🐲"
                            "Rei Supremo" -> "👑"
                            else -> "🗡️"
                        }

                        Text(
                            text = emojiSkin,
                            fontSize = 42.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Herói (${uiState.skinEquipada})",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = {
                            (uiState.vidaHeroiAtual / uiState.vidaHeroiMax).coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .width(180.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFF00B4DB),
                        trackColor = Color.DarkGray
                    )
                }
            }

            // Caixa de Log de Combate (Posicionada corretamente na parte inferior)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.logCombate,
                        color = Color(0xFFE2E8F0),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
            }
            Button(
                onClick = onGolpeEspecial, // Certifique-se de passar a função do ViewModel ou chamá-la via parâmetro da tela
                enabled = uiState.podeAtacarEspecial,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800),
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (uiState.podeAtacarEspecial) "⚡ GOLPE FÚRIA (Crítico)" else "⏳ Recarregando Habilidade...",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}