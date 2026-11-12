package com.example.idleautobattler.telas.loja

data class LojaUiState(
    val itensExibidos: List<ItemLojaEntity> = emptyList(),
    val filtroTipo: String = "TODOS",
    val ehAdmin: Boolean = false,
    val ouroJogador: Int = 1250,
    val danoJogador: Int = 15
)
