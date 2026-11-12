package com.example.idleautobattler.telas.combate

data class CombateUiState(
    val ouro: Int = 1250,
    val dano: Int = 15,
    val vidaHeroiAtual: Float = 100f,
    val vidaHeroiMax: Float = 100f,
    val vidaMonstroAtual: Float = 200f,
    val vidaMonstroMax: Float = 200f,
    val nomeMonstro: String = "Dragão de Cristal",
    val logCombate: String = "A batalha automática começou!",
    val animarAtaqueMonstro: Boolean = false,
    val animarAtaqueHeroi: Boolean = false,
    val eventoBonusTexto: String = "Bônus de Evento Ativo",
    val skinEquipada: String = "Herói Padrão",
    val podeAtacarEspecial: Boolean = true,

// Novos campos para o Sistema de Fases
val faseAtual: Int = 1,
val monstrosDerrotadosNaFase: Int = 0,
val monstrosNecessariosParaProximaFase: Int = 5,

val isLoading: Boolean = false,
val mensagemErro: String? = null
)