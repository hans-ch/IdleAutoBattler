package com.example.idleautobattler.telas.loja

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LojaScreen(
    ouro: Int = 1250,
    danoAtual: Int = 15,
    itens: List<ItemLojaEntity> = emptyList(),
    ehAdmin: Boolean = false,
    filtroAtual: String = "TODOS",
    onComprarUpgrade: (ItemLojaEntity) -> Unit = {},
    onDeletarItem: (ItemLojaEntity) -> Unit = {},
    onCriarItem: (String, Int, String, Int) -> Unit = { _, _, _, _ -> },
    onAlterarFiltro: (String) -> Unit = {}
) {
    var exibeDialogNovoItem by remember { mutableStateOf(false) }
    var novoTitulo by remember { mutableStateOf("") }
    var novoPreco by remember { mutableStateOf("100") }
    var novoDano by remember { mutableStateOf("15") }

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
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🛒 Loja de Upgrades",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                if (ehAdmin) {
                    IconButton(
                        onClick = { exibeDialogNovoItem = true },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF00FFCC)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Novo Item", tint = Color.Black, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Ouro", tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$ouro Ouro",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = Color(0xFF00FFCC).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Dano: $danoAtual DPS",
                            color = Color(0xFF00FFCC),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("TODOS", "DANO", "VIDA").forEach { tipo ->
                        FilterChip(
                            selected = filtroAtual == tipo,
                            onClick = { onAlterarFiltro(tipo) },
                            label = { Text(tipo, fontSize = 11.sp) }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itens, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = item.titulo,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 18.sp
                                )
                                Text(
                                    text = "Nível atual: ${item.nivelAtual}",
                                    color = Color(0xFF00FFCC),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val podeComprar = ouro >= item.precoAtual
                                Button(
                                    onClick = { onComprarUpgrade(item) },
                                    enabled = podeComprar,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF9800),
                                        disabledContainerColor = Color(0xFF334155)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                ) {
                                    Text(
                                        text = "${item.precoAtual} Ouro",
                                        color = if (podeComprar) Color.White else Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                }

                                if (ehAdmin) {
                                    IconButton(
                                        onClick = { onDeletarItem(item) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Deletar Item",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(20.dp)
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

    if (exibeDialogNovoItem) {
        AlertDialog(
            onDismissRequest = { exibeDialogNovoItem = false },
            title = { Text("Criar Novo Item (Admin)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = novoTitulo,
                        onValueChange = { novoTitulo = it },
                        label = { Text("Título do Item") }
                    )
                    OutlinedTextField(
                        value = novoPreco,
                        onValueChange = { novoPreco = it },
                        label = { Text("Preço em Ouro") }
                    )
                    OutlinedTextField(
                        value = novoDano,
                        onValueChange = { novoDano = it },
                        label = { Text("Incremento de DPS") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (novoTitulo.isNotBlank()) {
                            onCriarItem(
                                novoTitulo,
                                novoPreco.toIntOrNull() ?: 100,
                                "DANO",
                                novoDano.toIntOrNull() ?: 15
                            )
                            exibeDialogNovoItem = false
                        }
                    }
                ) {
                    Text("Salvar no Banco")
                }
            },
            dismissButton = {
                TextButton(onClick = { exibeDialogNovoItem = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
