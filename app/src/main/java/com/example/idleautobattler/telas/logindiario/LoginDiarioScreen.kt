package com.example.idleautobattler.telas.logindiario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RecompensaDiaria(
    val dia: Int,
    val recompensa: String,
    val quantidade: String,
    val icon: ImageVector,
    val ehEspecial: Boolean = false
)

@Composable
fun LoginDiarioScreen() {
    var diaAtual by remember { mutableIntStateOf(3) }
    var resgatadoHoje by remember { mutableStateOf(false) }

    val recompensas = remember {
        listOf(
            RecompensaDiaria(1, "Ouro", "+100 Ouro", Icons.Default.Star),
            RecompensaDiaria(2, "Ouro", "+250 Ouro", Icons.Default.Star),
            RecompensaDiaria(3, "Bônus Ouro", "+500 Ouro", Icons.Default.Star),
            RecompensaDiaria(4, "Ouro", "+1.000 Ouro", Icons.Default.Star),
            RecompensaDiaria(5, "Baú de Itens", "1x Baú Épico", Icons.Default.CardGiftcard, true),
            RecompensaDiaria(6, "Super Ouro", "+2.500 Ouro", Icons.Default.Star),
            RecompensaDiaria(7, "Espada Rara", "Espada Lendária", Icons.Default.WorkspacePremium, true)
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🎁 Login Diário",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xFFFF9800).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9800))
                ) {
                    Text(
                        text = "🔥 Sequência Atual: $diaAtual Dias Consecutivos",
                        color = Color(0xFFFF9800),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Calendário de Recompensas de 7 Dias",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(300.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(recompensas) { index, item ->
                            val diaNumber = index + 1
                            val status = when {
                                diaNumber < diaAtual -> StatusRecompensa.RESGATADO
                                (diaNumber == diaAtual) && resgatadoHoje -> StatusRecompensa.RESGATADO
                                diaNumber == diaAtual -> StatusRecompensa.DISPONIVEL
                                else -> StatusRecompensa.BLOQUEADO
                            }

                            ItemCardRecompensa(
                                recompensa = item,
                                status = status,
                                isGrandPrize = (diaNumber == 7)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!resgatadoHoje) {
                    Button(
                        onClick = { resgatadoHoje = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RESGATAR RECOMPENSA DO DIA $diaAtual!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20).copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF66BB6A))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Recompensa do Dia $diaAtual Resgatada! 🎉",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Text(
                    text = "Próxima recompensa disponível em 14h 22m 30s",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

enum class StatusRecompensa {
    RESGATADO, DISPONIVEL, BLOQUEADO
}

@Composable
private fun ItemCardRecompensa(
    recompensa: RecompensaDiaria,
    status: StatusRecompensa,
    isGrandPrize: Boolean = false
) {
    val containerColor = when (status) {
        StatusRecompensa.RESGATADO -> Color(0xFF2E7D32).copy(alpha = 0.4f)
        StatusRecompensa.DISPONIVEL -> Color(0xFFFF8F00).copy(alpha = 0.5f)
        StatusRecompensa.BLOQUEADO -> Color.Black.copy(alpha = 0.3f)
    }

    val borderColor = when {
        isGrandPrize -> Color(0xFFFFD700)
        status == StatusRecompensa.RESGATADO -> Color(0xFF4CAF50)
        status == StatusRecompensa.DISPONIVEL -> Color(0xFFFF8F00)
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .border(
                width = if (status != StatusRecompensa.BLOQUEADO) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Dia ${recompensa.dia}",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (recompensa.ehEspecial) Color(0xFFFFD700).copy(alpha = 0.2f)
                            else Color.White.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = recompensa.icon,
                        contentDescription = null,
                        tint = if (recompensa.ehEspecial) Color(0xFFFFD700) else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = recompensa.quantidade,
                    color = if (status == StatusRecompensa.BLOQUEADO) Color.Gray else Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (status == StatusRecompensa.RESGATADO) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Resgatado",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(30.dp)
                    )
                }
            } else if (status == StatusRecompensa.BLOQUEADO) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
