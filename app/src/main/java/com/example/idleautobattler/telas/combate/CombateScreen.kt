package com.example.idleautobattler.telas.combate

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
fun CombateScreen(
    ouro: Int = 1250,
    dps: Int = 15,
    onGanharOuro: (Int) -> Unit = {},
    onPerderOuro: (Int) -> Unit = {}
) {
    val vidaHeroiMax = 100f
    var vidaHeroiAtual by remember { mutableFloatStateOf(100f) }

    var nomeMonstro by remember { mutableStateOf("Dragão de Cristal") }
    val vidaMonstroMax = 200f
    var vidaMonstroAtual by remember { mutableFloatStateOf(200f) }

    var logCombate by remember { mutableStateOf("A batalha automática começou!") }
    var animarAtaqueHeroi by remember { mutableStateOf(false) }

    LaunchedEffect(dps) {
        while (true) {
            delay(500)

            val danoPorGolpe = (dps / 2f).coerceAtLeast(1f)
            vidaMonstroAtual -= danoPorGolpe
            animarAtaqueHeroi = true
            logCombate = "Herói causou ${danoPorGolpe.toInt()} de dano em $nomeMonstro"

            delay(100)
            animarAtaqueHeroi = false

            if (vidaMonstroAtual <= 0f) {
                onGanharOuro(50)
                logCombate = "🎉 $nomeMonstro foi derrotado! +50 de Ouro."
                delay(1000)
                vidaMonstroAtual = vidaMonstroMax + 50f
                nomeMonstro = if (nomeMonstro == "Dragão de Cristal") "Rei Goblin Corrompido" else "Dragão de Cristal"
            }

            delay(600)
            vidaHeroiAtual -= 5f
            if (vidaHeroiAtual <= 0f) {
                logCombate = "⚠️ Herói foi derrotado! Recomeçando..."
                delay(1500)
                vidaHeroiAtual = vidaHeroiMax
                onPerderOuro(20)
            }
        }
    }

    val escalaHeroi by animateFloatAsState(
        targetValue = if (animarAtaqueHeroi) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaHeroi"
    )

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
                        Icon(Icons.Default.Star, contentDescription = "Ouro", tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$ouro", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    }
                }

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
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

                Text(
                    text = "⚔️ VS ⚔️",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge
                )

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
