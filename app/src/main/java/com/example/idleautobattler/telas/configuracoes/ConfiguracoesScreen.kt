package com.example.idleautobattler.telas.configuracoes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfiguracoesScreen(
    ehAdmin: Boolean = false,
    onAlternarAdmin: (Boolean) -> Unit = {},
    onFazerLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    var sfxAtivado by remember { mutableStateOf(true) }
    var musicaAtivada by remember { mutableStateOf(true) }
    var notificacoesAtivadas by remember { mutableStateOf(true) }
    var qualidadeGrafica by remember { mutableStateOf("Alta") }
    var exibeDialogReset by remember { mutableStateOf(false) }

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
            Text(
                text = "⚙️ Configurações do Jogo",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "🎵 Áudio e Som",
                color = Color(0xFFFFD700),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
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
                    SwitchConfigItem(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        titulo = "Efeitos Sonoros (SFX)",
                        subtitulo = "Sons de ataque e habilidades",
                        checked = sfxAtivado,
                        onCheckedChange = { sfxAtivado = it }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    SwitchConfigItem(
                        icon = Icons.Default.MusicNote,
                        titulo = "Música de Fundo",
                        subtitulo = "Trilha sonora da arena",
                        checked = musicaAtivada,
                        onCheckedChange = { musicaAtivada = it }
                    )
                }
            }

            Text(
                text = "📱 Geral & Notificações",
                color = Color(0xFFFFD700),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
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
                    SwitchConfigItem(
                        icon = Icons.Default.Notifications,
                        titulo = "Notificações de Recompensa",
                        subtitulo = "Avisar quando o Login Diário estiver disponível",
                        checked = notificacoesAtivadas,
                        onCheckedChange = { notificacoesAtivadas = it }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    SwitchConfigItem(
                        icon = Icons.Default.AdminPanelSettings,
                        titulo = "Perfil Administrador (Admin)",
                        subtitulo = "Habilita criação e deleção de itens no banco de dados",
                        checked = ehAdmin,
                        onCheckedChange = onAlternarAdmin
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = Color(0xFF00FFCC))
                            Column {
                                Text("Qualidade Gráfica", color = Color.White, fontSize = 15.sp)
                                Text("Ajusta performance do jogo", color = Color.Gray, fontSize = 12.sp)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("Baixa", "Média", "Alta").forEach { opcao ->
                                FilterChip(
                                    selected = qualidadeGrafica == opcao,
                                    onClick = { qualidadeGrafica = opcao },
                                    label = { Text(opcao, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "⚠️ Dados do Progresso",
                color = Color(0xFFFF5722),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
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
                    Button(
                        onClick = { exibeDialogReset = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Resetar Progresso de Combate", color = Color.White)
                    }

                    Button(
                        onClick = onFazerLogout,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sair / Log-out da Conta", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.LightGray)
                    Column {
                        Text("Idle Auto Battler v1.0.0", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Desenvolvido com Jetpack Compose & Material 3", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    if (exibeDialogReset) {
        AlertDialog(
            onDismissRequest = { exibeDialogReset = false },
            title = { Text("Confirmar Reset?") },
            text = { Text("Tem certeza que deseja apagar todo o progresso do jogo? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        exibeDialogReset = false
                    }
                ) {
                    Text("Sim, Resetar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { exibeDialogReset = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SwitchConfigItem(
    icon: ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00FFCC))
            Column {
                Text(text = titulo, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitulo, color = Color.Gray, fontSize = 12.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF00B4DB)
            )
        )
    }
}
