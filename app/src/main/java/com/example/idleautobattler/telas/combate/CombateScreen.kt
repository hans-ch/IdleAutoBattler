package com.example.idleautobattler.telas.combate

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.graphicsLayer
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
    // Cores de ambiente modernas por fase
    val (corPrincipal, corSecundaria, corIluminacao) = when (uiState.faseAtual % 3) {
        1 -> Triple(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF38BDF8)) // Caverna Néon / Ciano
        2 -> Triple(Color(0xFF064E3B), Color(0xFF022C22), Color(0xFF34D399)) // Floresta Sombria / Esmeralda
        else -> Triple(Color(0xFF450A0A), Color(0xFF18181B), Color(0xFFF87171)) // Vulcão / Carmim
    }

    // Animação de pulsar do brilho de fundo
    val infiniteTransition = rememberInfiniteTransition(label = "brilho")
    val alphaBrilho by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaBrilho"
    )

    // Animações de escala para o Herói e para o Monstro
    val escalaHeroi by animateFloatAsState(
        targetValue = if (uiState.animarAtaqueHeroi) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaHeroi"
    )
    val escalaMonstro by animateFloatAsState(
        targetValue = if (uiState.animarAtaqueMonstro) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaMonstro"
    )
    // Animação de morte / desaparecimento do herói
    val alphaHeroi by animateFloatAsState(
        targetValue = if (uiState.vidaHeroiAtual <= 0f) 0.2f else 1.0f,
        animationSpec = tween(500),
        label = "alphaHeroi"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(corPrincipal, corSecundaria, Color.Black)
                )
            )
    ) {
        // --- 1. ILUMINAÇÃO DE FUNDO (Luz Ambiental no Centro) ---
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.Center)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            corIluminacao.copy(alpha = alphaBrilho),
                            Color.Transparent
                        )
                    )
                )
        )

        // --- 2. CONTEÚDO DA TELA ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HUD Superior (Vidro Flutuante com Bordas Iluminadas)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card de Ouro
                Surface(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Ouro", tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("${uiState.ouro}", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Indicador de Fase
                Surface(
                    color = corIluminacao.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.border(1.dp, corIluminacao.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = "Fase ${uiState.faseAtual} (${uiState.monstrosDerrotadosNaFase}/${uiState.monstrosNecessariosParaProximaFase})",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Card de DPS
                Surface(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = "${uiState.dano} DPS",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }

            // --- 3. CAMPO DE BATALHA (MONSTRO VS HERÓI) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ARENA MONSTRO (Com animação de ataque aplicada)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(escalaMonstro)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Sombra de Chão
                        Box(
                            modifier = Modifier
                                .size(110.dp, 30.dp)
                                .offset(y = 40.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                                    )
                                )
                        )
                        // Aura Vermelha do Monstro
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444).copy(alpha = 0.2f))
                                .border(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val emojiMonstro = when (uiState.nomeMonstro) {
                                "Dragão de Cristal" -> "🐉"
                                "Rei Goblin Corrompido" -> "👺"
                                "Esqueleto Guerreiro" -> "💀"
                                "Golem de Pedra" -> "🗿"
                                else -> "👾"
                            }
                            Text(text = emojiMonstro, fontSize = 48.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = uiState.nomeMonstro, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))

                    val vidaMonstroTotalMax = uiState.vidaMonstroMax + 50f
                    LinearProgressIndicator(
                        progress = { (uiState.vidaMonstroAtual / vidaMonstroTotalMax).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .width(220.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFFF43F5E),
                        trackColor = Color.Black.copy(alpha = 0.5f)
                    )
                }

                // VS Divisor com efeito Neon
                Text(
                    text = "⚔️",
                    fontSize = 26.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                // ARENA HERÓI (Com animação de ataque aplicada)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(escalaHeroi).graphicsLayer(alpha = alphaHeroi)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Sombra de Chão para o Herói
                        Box(
                            modifier = Modifier
                                .size(110.dp, 30.dp)
                                .offset(y = 40.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                                    )
                                )
                        )
                        // Aura Azul/Neon do Herói
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(corIluminacao.copy(alpha = 0.25f))
                                .border(2.dp, corIluminacao.copy(alpha = 0.6f), CircleShape),
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
                            Text(text = emojiSkin, fontSize = 48.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = uiState.skinEquipada, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { (uiState.vidaHeroiAtual / uiState.vidaHeroiMax).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .width(200.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFF0EA5E9),
                        trackColor = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }

            // --- 4. CONTROLES E LOG (RODAPÉ) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Caixa de Log em Glassmorphism
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp)),
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.logCombate,
                            color = Color(0xFFE2E8F0),
                            fontSize = 13.sp,
                            maxLines = 2
                        )
                    }
                }

                // Botão Golpe Fúria Moderno
                Button(
                    onClick = onGolpeEspecial,
                    enabled = uiState.podeAtacarEspecial,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B),
                        disabledContainerColor = Color(0xFF334155)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (uiState.podeAtacarEspecial) "⚡ GOLPE FÚRIA" else "⏳ RECARREGANDO HABILIDADE...",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}