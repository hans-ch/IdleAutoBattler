package com.example.idleautobattler.telas.loja

import com.example.idleautobattler.data.ItemLojaEntity

data class LojaUiState(
    val itensExibidos: List<ItemLojaEntity> = emptyList(),
    val filtroTipo: String = "TODOS",
    val ordenarPorPreco: Boolean = false,
    val ehAdmin: Boolean = false,
    val ouroJogador: Int = 1250,
    val danoJogador: Int = 15,
    val isLoading: Boolean = false,
    val mensagemErro: String? = null
)
