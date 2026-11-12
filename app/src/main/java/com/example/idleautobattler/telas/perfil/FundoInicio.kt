package com.example.idleautobattler.telas.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.idleautobattler.data.SkinEntity
import kotlinx.coroutines.delay

private val PerfilFundoInicio = Color(0xFF1A1A2E)
private val PerfilFundoMeio = Color(0xFF16213E)
private val PerfilFundoFim = Color(0xFF0F3460)
private val PerfilDourado = Color(0xFFFFD700)
private val PerfilVerdeXp = Color(0xFF00FFCC)


@Composable
private fun PerfilCabecalho(
    nome: String,
    ouro: Int,
    skinEquipada: SkinEntity?,
    onEditarNome: () -> Unit
) {
    val xpAtual = 4500
    val xpNecessario = 5000

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.48f)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF00B4DB),
                                Color(0xFF0083B0)
                            )
                        )
                    )
                    .border(2.dp, PerfilDourado, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = skinEquipada?.iconeEmoji ?: "⚔️",
                    fontSize = 36.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = nome,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(
                        onClick = onEditarNome,
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("Editar")
                    }
                }

                Text(
                    text = "Skin: ${skinEquipada?.nome ?: "Herói Padrão"}",
                    color = PerfilDourado,
                    fontSize = 13.sp
                )

                Text(
                    text = "Ouro: $ouro",
                    color = Color(0xFFFFE082),
                    fontSize = 13.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("XP", color = Color.LightGray, fontSize = 11.sp)
                    Text(
                        "$xpAtual / $xpNecessario",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }

                LinearProgressIndicator(
                    progress = { xpAtual.toFloat() / xpNecessario },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PerfilVerdeXp,
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
private fun SeletorSkinsPerfil(
    skins: List<SkinEntity>,
    skinEquipada: SkinEntity?,
    ouro: Int,
    onSelecionarSkin: (SkinEntity) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        items(
            items = skins,
            key = { skin -> skin.id }
        ) { skin ->
            val estaEquipada = skin.id == skinEquipada?.id
            val podeComprar = ouro >= skin.precoOuro

            Card(
                modifier = Modifier.width(145.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (estaEquipada) {
                        Color(0xFF174A48)
                    } else {
                        Color.Black.copy(alpha = 0.48f)
                    }
                ),
                border = if (estaEquipada) {
                    androidx.compose.foundation.BorderStroke(
                        1.dp,
                        PerfilVerdeXp
                    )
                } else {
                    null
                },
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = skin.iconeEmoji,
                        fontSize = 30.sp
                    )

                    Text(
                        text = skin.nome,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = skin.raritade,
                        color = corRaridadePerfil(skin.raritade),
                        fontSize = 12.sp
                    )

                    Button(
                        onClick = { onSelecionarSkin(skin) },
                        enabled = !estaEquipada &&
                                (skin.desbloqueada || podeComprar),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when {
                                estaEquipada -> "Equipada"
                                skin.desbloqueada -> "Equipar"
                                else -> "${skin.precoOuro} ouro"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TituloSecaoPerfil(texto: String) {
    Text(
        text = texto,
        color = Color.White,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun CardSecaoPerfil(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
private fun ItemAtributoPerfil(
    icon: ImageVector,
    nome: String,
    valor: String,
    cor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = nome,
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Text(
            text = valor,
            color = cor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun RowStatPerfil(
    label: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 13.sp
        )

        Text(
            text = valor,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

private fun corRaridadePerfil(raridade: String): Color =
    when (raridade.lowercase()) {
        "lendária", "lendario" -> Color(0xFFFF9800)
        "rara" -> Color(0xFF2196F3)
        "épica", "epica" -> Color(0xFFAB47BC)
        else -> Color.Gray
    }