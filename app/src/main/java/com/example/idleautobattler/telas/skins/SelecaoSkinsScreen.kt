package com.example.idleautobattler.telas.skins

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelecaoSkinsScreen(
    skins: List<SkinEntity>,
    skinEquipadaAtual: SkinEntity?,
    ouroDisponivel: Int,
    onSelecionarSkin: (SkinEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Guarda-Roupa de Skins",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Skin em uso: ${skinEquipadaAtual?.nome ?: "Nenhuma"}",
                        color = Color(0xFF00FFCC),
                        fontSize = 13.sp
                    )
                }

                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = ouroDisponivel.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (skins.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma skin disponível.",
                        color = Color.LightGray
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = skins,
                        key = { skin -> skin.id }
                    ) { skin ->
                        val estaEquipada = skin.id == skinEquipadaAtual?.id
                        val podeComprar = ouroDisponivel >= skin.precoOuro
                        val podeSelecionar = !estaEquipada &&
                                (skin.desbloqueada || podeComprar)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .border(
                                    width = if (estaEquipada) 2.dp else 0.dp,
                                    color = if (estaEquipada) {
                                        Color(0xFF00FFCC)
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    color = corRaridadeSkin(skin.raritade)
                                        .copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = skin.raritade,
                                        color = corRaridadeSkin(skin.raritade),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 6.dp,
                                            vertical = 2.dp
                                        )
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = skin.iconeEmoji,
                                        fontSize = 28.sp
                                    )
                                }

                                Text(
                                    text = skin.nome,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Button(
                                    onClick = {
                                        onSelecionarSkin(skin)
                                    },
                                    enabled = podeSelecionar,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = when {
                                            estaEquipada -> Color(0xFF00C853)
                                            skin.desbloqueada -> Color(0xFF00B4DB)
                                            else -> Color(0xFFFF9800)
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    when {
                                        estaEquipada -> {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Equipada",
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }

                                        skin.desbloqueada -> {
                                            Text(
                                                text = "Equipar",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        else -> {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lock,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${skin.precoOuro} ouro",
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun corRaridadeSkin(raridade: String): Color =
    when (raridade.lowercase()) {
        "lendária", "lendario" -> Color(0xFFFF9800)
        "épica", "epica" -> Color(0xFF9C27B0)
        "rara" -> Color(0xFF2196F3)
        else -> Color.Gray
    }
