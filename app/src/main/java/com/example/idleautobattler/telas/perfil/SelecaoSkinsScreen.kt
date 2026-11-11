package com.example.idleautobattler.telas.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ItemSkin(
    val id: Int,
    val nome: String,
    val emoji: String,
    val raridade: String,
    val precoOuro: Int,
    val corRaridade: Color,
    val estaDesbloqueada: Boolean = false
)

@Composable
fun SelecaoSkinsScreen(
    skinEquipadaAtual: String = "Herói Padrão",
    ouroDisponivel: Int = 1250,
    onEquiparSkin: (String) -> Unit = {}
) {
    var skinEquipada by remember { mutableStateOf(skinEquipadaAtual) }

    val skins = remember {
        listOf(
            ItemSkin(1, "Herói Padrão", "🗡️", "Comum", 0, Color.Gray, true),
            ItemSkin(2, "Mago de Fogo", "🧙‍♂️", "Raro", 300, Color(0xFF2196F3), true),
            ItemSkin(3, "Ninja das Sombras", "🥷", "Épico", 750, Color(0xFF9C27B0), false),
            ItemSkin(4, "Cyborg RPG", "🤖", "Épico", 1500, Color(0xFF9C27B0), false),
            ItemSkin(5, "Matador de Dragões", "🐲", "Lendário", 3000, Color(0xFFFFD700), false),
            ItemSkin(6, "Rei Supremo", "👑", "Lendário", 5000, Color(0xFFFFD700), false)
        )
    }

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
                        text = "🎨 Guarda-Roupa de Skins",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Skin em uso: $skinEquipada",
                        color = Color(0xFF00FFCC),
                        fontSize = 13.sp
                    )
                }

                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$ouroDisponivel", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(skins) { skin ->
                    val ehEquipada = (skin.nome == skinEquipada)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(
                                width = if (ehEquipada) 2.dp else 0.dp,
                                color = if (ehEquipada) Color(0xFF00FFCC) else Color.Transparent,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
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
                                color = skin.corRaridade.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = skin.raridade,
                                    color = skin.corRaridade,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = skin.emoji, fontSize = 28.sp)
                            }

                            Text(
                                text = skin.nome,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            if (skin.estaDesbloqueada) {
                                if (ehEquipada) {
                                    Button(
                                        onClick = {},
                                        enabled = false,
                                        colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFF00C853).copy(alpha = 0.3f)),
                                        modifier = Modifier.fillMaxWidth().height(32.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Equipada", fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            skinEquipada = skin.nome
                                            onEquiparSkin(skin.nome)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4DB)),
                                        modifier = Modifier.fillMaxWidth().height(32.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("Equipar", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else {
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                                    modifier = Modifier.fillMaxWidth().height(32.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${skin.precoOuro} Ouro", fontSize = 11.sp, color = Color.White)
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
