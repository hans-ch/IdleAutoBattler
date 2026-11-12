package com.example.idleautobattler.telas.navegacao

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.idleautobattler.data.AppDatabase
import com.example.idleautobattler.data.AppViewModelFactory
import com.example.idleautobattler.data.JogoRepositoryImpl
import com.example.idleautobattler.data.RetrofitClient
import com.example.idleautobattler.telas.combate.CombateScreen
import com.example.idleautobattler.telas.combate.CombateViewModel
import com.example.idleautobattler.telas.configuracoes.ConfiguracoesScreen
import com.example.idleautobattler.telas.login.LoginScreen
import com.example.idleautobattler.telas.loja.LojaScreen
import com.example.idleautobattler.telas.loja.LojaViewModel
import com.example.idleautobattler.telas.perfil.PerfilRepository
import com.example.idleautobattler.telas.perfil.PerfilScreen
import com.example.idleautobattler.telas.perfil.PerfilViewModel
import com.example.idleautobattler.telas.skins.SelecaoSkinsScreen

sealed class Tela(
    val rota: String,
    val titulo: String,
    val icon: ImageVector
) {
    object Login : Tela("login", "Login", Icons.AutoMirrored.Filled.Login)
    object Combate : Tela("combate", "Combate", Icons.Default.SportsEsports)
    object Loja : Tela("loja", "Loja", Icons.Default.ShoppingCart)
    object Perfil : Tela("perfil", "Perfil", Icons.Default.Person)
    object Skins : Tela("skins", "Skins", Icons.Default.Palette)
    object Configuracoes : Tela("configuracoes", "Configurações", Icons.Default.Settings)
}

val telasBarraNavegacao = listOf(
    Tela.Combate,
    Tela.Loja,
    Tela.Perfil,
    Tela.Skins,
    Tela.Configuracoes
)

@Composable
fun NavegacaoPrincipal() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val database = remember {
        AppDatabase.getInstance(context)
    }

    val jogoRepository = remember {
        JogoRepositoryImpl(
            progressoDao = database.progressoDao(),
            itemLojaDao = database.itemLojaDao(),
            skinDao = database.skinDao(),
            apiService = RetrofitClient.apiService
        )
    }

    val jogoFactory = remember {
        AppViewModelFactory(jogoRepository)
    }

    val perfilRepository = remember {
        PerfilRepository(database)
    }

    val perfilFactory = remember(perfilRepository) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
                    return PerfilViewModel(perfilRepository) as T
                }

                throw IllegalArgumentException(
                    "ViewModel desconhecido: ${modelClass.name}"
                )
            }
        }
    }

    val combateViewModel: CombateViewModel = viewModel(factory = jogoFactory)
    val lojaViewModel: LojaViewModel = viewModel(factory = jogoFactory)
    val perfilViewModel: PerfilViewModel = viewModel(factory = perfilFactory)

    val combateState by combateViewModel.uiState.collectAsState()
    val lojaState by lojaViewModel.uiState.collectAsState()
    val perfilState by perfilViewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Tela.Login.rota) {
                NavigationBar(
                    containerColor = Color(0xFF0F172A),
                    contentColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    telasBarraNavegacao.forEach { tela ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = tela.icon,
                                    contentDescription = tela.titulo
                                )
                            },
                            label = {
                                Text(text = tela.titulo)
                            },
                            selected = currentRoute == tela.rota,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF00FFCC),
                                selectedTextColor = Color(0xFF00FFCC),
                                indicatorColor = Color(0xFF1E293B),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            ),
                            onClick = {
                                if (currentRoute != tela.rota) {
                                    navController.navigate(tela.rota) {
                                        popUpTo(
                                            navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Tela.Login.rota,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Tela.Login.rota) {
                LoginScreen(
                    onLoginSucesso = { nomeJogador ->
                        perfilViewModel.inicializar(nomeJogador)

                        navController.navigate(Tela.Combate.rota) {
                            popUpTo(Tela.Login.rota) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Tela.Combate.rota) {
                CombateScreen(
                    uiState = combateState,
                    onGanharOuro = { combateViewModel.ganharOuro(it) },
                    onPerderOuro = { combateViewModel.perderOuro(it) },
                    onGolpeEspecial = { combateViewModel.desferirGolpeEspecial() }
                )
            }

            composable(Tela.Loja.rota) {
                LojaScreen(
                    ouro = lojaState.ouroJogador,
                    danoAtual = lojaState.danoJogador,
                    itens = lojaState.itensExibidos,
                    ehAdmin = lojaState.ehAdmin,
                    filtroAtual = lojaState.filtroTipo,
                    onComprarUpgrade = { lojaViewModel.comprarUpgrade(it) },
                    onDeletarItem = { lojaViewModel.deletarItem(it) },
                    onCriarItem = { t, p, tipo, d -> lojaViewModel.criarNovoItem(t, p, tipo, d) },
                    onAlterarFiltro = { lojaViewModel.alterarFiltro(it) }
                )
            }

            composable(Tela.Perfil.rota) {
                PerfilScreen(
                    viewModel = perfilViewModel,
                    nomeInicial = perfilState.nome
                )
            }

            composable(Tela.Skins.rota) {
                SelecaoSkinsScreen(
                    skins = perfilState.skins,
                    skinEquipadaAtual = perfilState.skinEquipada,
                    ouroDisponivel = perfilState.ouro,
                    onSelecionarSkin = perfilViewModel::comprarOuEquipar
                )
            }

            composable(Tela.Configuracoes.rota) {
                ConfiguracoesScreen(
                    ehAdmin = lojaState.ehAdmin,
                    onAlternarAdmin = {
                        lojaViewModel.alternarModoAdmin()
                    },
                    onFazerLogout = {
                        navController.navigate(Tela.Login.rota) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}
