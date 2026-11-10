package com.example.idleautobattler.Screen_Combat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun TelaAutoBattlerImersiva() {
    // Estados do Jogo (Simulando o Auto-Battler)
    var ouro by remember { mutableIntStateOf(1250) }
    var dps by remember { mutableIntStateOf(15) }

    // Status do Herói
    val vidaHeroiMax = 100f
    var vidaHeroiAtual by remember { mutableFloatStateOf(100f) }

    // Status do Inimigo
    var nomeMonstro by remember { mutableStateOf("Dragão de Cristal") }
    val vidaMonstroMax = 200f
    var vidaMonstroAtual by remember { mutableFloatStateOf(200f) }

    // Log de Combate e Feedback Visual
    var logCombate by remember { mutableStateOf("A batalha automática começou!") }
    var animarAtaqueHeroi by remember { mutableStateOf(false) }

    // Loop do Auto-Battler (Roda sozinho em background)
    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // A cada 0.5 segundos o combate acontece

            // Herói ataca o monstro
            vidaMonstroAtual -= (dps / 2f)
            animarAtaqueHeroi = true
            logCombate = "Herói causou ${dps / 2} de dano em $nomeMonstro"

            // Reseta animação rápida
            delay(100)
            animarAtaqueHeroi = false

            // Verifica se o monstro morreu
            if (vidaMonstroAtual <= 0f) {
                ouro += 50
                logCombate = "🎉 $nomeMonstro foi derrotado! +50 de Ouro."
                delay(1000)
                // Spawn de novo monstro mais forte
                vidaMonstroAtual = vidaMonstroMax + 50f
                nomeMonstro = if (nomeMonstro == "Dragão de Cristal") "Rei Goblin Corrompido" else "Dragão de Cristal"
            }

            // Monstro contra-ataca o herói periodicamente
            delay(600)
            vidaHeroiAtual -= 5f
            if (vidaHeroiAtual <= 0f) {
                logCombate = "⚠️ Herói foi derrotado! Recomeçando..."
                delay(1500)
                vidaHeroiAtual = vidaHeroiMax
                ouro = (ouro - 20).coerceAtLeast(0) // Penalidade leve de ouro
            }
        }
    }

    // Escala animada para o ataque do herói
    val escalaHeroi by animateFloatAsState(
        targetValue = if (animarAtaqueHeroi) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaHeroi"
    )

    // Layout com visual estilo "Arena de RPG"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOPO: HUD de Recursos (Ouro e DPS) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card de Ouro
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Ouro", tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$ouro", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }
                }

                // Card de DPS Automático
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "DPS: $dps/s",
                        color = Color(0xFF00FFCC),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // --- CENTRO: A ARENA DE BATALHA ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Inimigo (Topo da Arena)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Inimigo",
                            modifier = Modifier.size(50.dp),
                            tint = Color(0xFFFF416C)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = nomeMonstro, color = Color.White, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Barra de Vida do Monstro
                    LinearProgressIndicator(
                        progress = { (vidaMonstroAtual / (vidaMonstroMax + 50f)).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .width(220.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = Color(0xFFFF416C),
                        trackColor = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${vidaMonstroAtual.toInt()} / ${(vidaMonstroMax + 50f).toInt()}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                // Indicador visual VS
                Text(
                    text = "⚔️ VS ⚔️",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Herói (Parte inferior da Arena)
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
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Herói",
                            modifier = Modifier.size(50.dp),
                            tint = Color(0xFF00B4DB)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Herói Automático", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Barra de Vida do Herói
                    LinearProgressIndicator(
                        progress = { (vidaHeroiAtual / vidaHeroiMax).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .width(180.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFF00B4DB),
                        trackColor = Color.DarkGray
                    )
                }
            }

            // --- ABAIXO: Caixa de Log de Combate (Estilo RPG) ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = logCombate,
                        color = Color(0xFFE2E8F0),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
            }
        }
    }
}