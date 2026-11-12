package com.example.idleautobattler.telas.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kotlinx.coroutines.delay

private val PerfilFundo = listOf(
    Color(0xFF1A1A2E),
    Color(0xFF16213E),
    Color(0xFF0F3460)
)

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    nomeInicial: String = "Guerreiro"
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    var mostrarEdicaoNome by remember { mutableStateOf(false) }
    var novoNome by remember(uiState.nome) {
        mutableStateOf(uiState.nome)
    }

    LaunchedEffect(nomeInicial) {
        viewModel.inicializar(nomeInicial)
    }

    LaunchedEffect(mensagem) {
        if (mensagem != null) {
            delay(2500)
            viewModel.limparMensagem()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(PerfilFundo))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PerfilCabecalho(
                uiState = uiState,
                onEditarNome = {
                    novoNome = uiState.nome
                    mostrarEdicaoNome = true
                }
            )

            Text(
                text = "Atributos principais",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            CardSecao {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ItemAtributo(
                        icon = Icons.Default.Star,
                        nome = "Dano",
                        valor = "${uiState.progresso?.dano ?: 15}",
                        cor = Color(0xFFFF5722)
                    )
                    ItemAtributo(
                        icon = Icons.Default.Shield,
                        nome = "Vida máxima",
                        valor = "${uiState.progresso?.vidaMaxima ?: 100} HP",
                        cor = Color(0xFF4CAF50)
                    )
                    ItemAtributo(
                        icon = Icons.Default.MilitaryTech,
                        nome = "Nível do monstro",
                        valor = "${uiState.progresso?.nivelMonstro ?: 1}",
                        cor = Color(0xFF2196F3)
                    )
                    ItemAtributo(
                        icon = Icons.Default.ThumbUp,
                        nome = "Ouro disponível",
                        valor = "${uiState.ouro}",
                        cor = Color(0xFFFFD700)
                    )
                }
            }

            Text(
                text = "Aparência e skins",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            SeletorSkins(
                skins = uiState.skins,
                skinEquipada = uiState.skinEquipada,
                ouro = uiState.ouro,
                onSelecionarSkin = viewModel::comprarOuEquipar
            )

            Text(
                text = "Estatísticas gerais",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            CardSecao {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RowStat("Monstros derrotados", "1.420")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                    RowStat("Ouro total coletado", "48.500")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                    RowStat("Fase máxima alcançada", "Fase 35")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
                    RowStat("Prestígios realizados", "2")
                }
            }
        }

        mensagem?.let { texto ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(texto)
            }
        }
    }

    if (mostrarEdicaoNome) {
        AlertDialog(
            onDismissRequest = { mostrarEdicaoNome = false },
            title = { Text("Editar nome") },
            text = {
                OutlinedTextField(
                    value = novoNome,
                    onValueChange = { novoNome = it },
                    label = { Text("Nome do jogador") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarEdicaoNome = false
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarEdicaoNome = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PerfilCabecalho(
    uiState: PerfilUiState,
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
                    .border(2.dp, Color(0xFFFFD700), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.skinEquipada?.iconeEmoji ?: "🗡️",
                    fontSize = 36.sp
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = uiState.nome,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(
                        onClick = onEditarNome,
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("Editar")
                    }
                }

                Text(
                    text = "Skin: ${uiState.skinEquipada?.nome ?: "Herói Padrão"}",
                    color = Color(0xFFFFD700),
                    fontSize = 13.sp
                )

                Text(
                    text = "Ouro: ${uiState.ouro}",
                    color = Color(0xFFFFE082),
                    fontSize = 13.sp
                )

                Text(
                    text = "Nível 15",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                LinearProgressIndicator(
                    progress = { xpAtual.toFloat() / xpNecessario },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF00FFCC),
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )

                Text(
                    text = "XP: $xpAtual / $xpNecessario",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun SeletorSkins(
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
            val equipada = skin.id == skinEquipada?.id
            val podeComprar = ouro >= skin.precoOuro

            Card(
                modifier = Modifier.width(150.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (equipada) {
                        Color(0xFF174A48)
                    } else {
                        Color.Black.copy(alpha = 0.48f)
                    }
                ),
                border = if (equipada) {
                    androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFF00FFCC)
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
                        enabled = !equipada && (skin.desbloqueada || podeComprar),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when {
                                equipada -> "Equipada"
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
private fun CardSecao(
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
private fun ItemAtributo(
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
private fun RowStat(
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
