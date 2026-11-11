package com.example.idleautobattler.telas.loja

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LojaScreen(
    ouro: Int = 1250,
    danoAtual: Int = 15,
    itens: List<ItemLojaEntity> = emptyList(),
    ehAdmin: Boolean = false,
    filtroAtual: String = "TODOS",
    ordenarPorPreco: Boolean = false,
    onComprarUpgrade: (ItemLojaEntity) -> Unit = {},
    onDeletarItem: (ItemLojaEntity) -> Unit = {},
    onCriarItem: (String, Int, String, Int) -> Unit = { _, _, _, _ -> },
    onAlterarFiltro: (String) -> Unit = {},
    onAlternarOrdenacao: () -> Unit = {}
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
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🛒 Loja de Upgrades",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                if (ehAdmin) {
                    IconButton(
                        onClick = { exibeDialogNovoItem = true },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF00FFCC))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Novo Item", tint = Color.Black)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Ouro", tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$ouro Ouro",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        color = Color(0xFF00FFCC).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Dano Atual: $danoAtual DPS",
                            color = Color(0xFF00FFCC),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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

                IconButton(onClick = onAlternarOrdenacao) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Ordenar",
                        tint = if (ordenarPorPreco) Color(0xFFFF9800) else Color.White
                    )
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
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = item.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Nível atual: ${item.nivelAtual}",
                                    color = Color(0xFF00FFCC),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { onComprarUpgrade(item) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${item.precoAtual} Ouro",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (ehAdmin) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { onDeletarItem(item) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Deletar Item", tint = Color.Red)
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
