package com.example.idleautobattler.telas.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen() {
    val scrollState = rememberScrollState()

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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFF00B4DB), Color(0xFF0083B0))
                                )
                            )
                            .border(2.dp, Color(0xFFFFD700), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(64.dp),
                            tint = Color.White
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Guerreiro Lendário",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Nível 15 • Cavaleiro de Elite",
                            color = Color(0xFFFFD700),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("XP", color = Color.LightGray, fontSize = 11.sp)
                                Text("4.500 / 5.000", color = Color.LightGray, fontSize = 11.sp)
                            }
                            LinearProgressIndicator(
                                progress = { 4500f / 5000f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF00FFCC),
                                trackColor = Color.Gray.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            Text(
                text = "⚔️ Atributos Principais",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ItemAtributo(icon = Icons.Default.Star, nome = "Dano por Segundo (DPS)", valor = "150 DPS", cor = Color(0xFFFF5722))
                    ItemAtributo(icon = Icons.Default.Shield, nome = "Vida Máxima", valor = "1.200 HP", cor = Color(0xFF4CAF50))
                    ItemAtributo(icon = Icons.Default.MilitaryTech, nome = "Defesa Armor", valor = "45 Armadura", cor = Color(0xFF2196F3))
                    ItemAtributo(icon = Icons.Default.ThumbUp, nome = "Bônus de Ouro", valor = "+25%", cor = Color(0xFFFFD700))
                }
            }

            Text(
                text = "🛡️ Equipamentos Equipados",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SlotEquipamento(nome = "Arma", item = "Espada de Fogo", raridade = "Lendário", corRaridade = Color(0xFFFF9800))
                SlotEquipamento(nome = "Armadura", item = "Peitoral de Aço", raridade = "Raro", corRaridade = Color(0xFF2196F3))
                SlotEquipamento(nome = "Elmo", item = "Capacete de Ferro", raridade = "Comum", corRaridade = Color.Gray)
            }

            Text(
                text = "📊 Estatísticas Gerais",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RowStat(label = "Monstros Derrotados", valor = "1.420")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    RowStat(label = "Ouro Total Coletado", valor = "48.500")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    RowStat(label = "Fase Máxima Alcançada", valor = "Fase 35")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    RowStat(label = "Prestígios Realizados", valor = "2 vezes")
                }
            }
        }
    }
}

@Composable
private fun ItemAtributo(icon: ImageVector, nome: String, valor: String, cor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = cor, modifier = Modifier.size(20.dp))
            Text(text = nome, color = Color.White, fontSize = 14.sp)
        }
        Text(text = valor, color = cor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun RowStat(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.LightGray, fontSize = 13.sp)
        Text(text = valor, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
private fun SlotEquipamento(nome: String, item: String, raridade: String, corRaridade: Color) {
    Card(
        modifier = Modifier
            .width(105.dp)
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = nome, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = item, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Surface(
                color = corRaridade.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = raridade,
                    color = corRaridade,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
