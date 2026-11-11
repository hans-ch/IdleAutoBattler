package com.example.idleautobattler.telas.combate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idleautobattler.data.JogoRepository
import com.example.idleautobattler.data.ProgressoEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CombateViewModel(
    private val repository: JogoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CombateUiState())
    val uiState: StateFlow<CombateUiState> = _uiState.asStateFlow()

    private var estaInicializandoProgresso = false
    private var jobCombate: Job? = null

    init {
        carregarDadosLocaisERemotos()
        iniciarLoopCombate() // Inicia a batalha automática assim que o ViewModel é criado
    }

    private fun carregarDadosLocaisERemotos() {
        viewModelScope.launch {
            try {
                repository.obterProgressoFlow().collect { progresso ->
                    if (progresso != null) {
                        _uiState.update {
                            it.copy(
                                ouro = progresso.ouro,
                                dano = progresso.dano,
                                skinEquipada = progresso.skinEquipada,
                                vidaHeroiAtual = if (it.vidaHeroiAtual > progresso.vidaMaxima.toFloat()) progresso.vidaMaxima.toFloat() else it.vidaHeroiAtual,
                                vidaHeroiMax = progresso.vidaMaxima.toFloat()
                            )
                        }
                    } else if (!estaInicializandoProgresso) {
                        estaInicializandoProgresso = true
                        repository.salvarProgresso(ProgressoEntity())
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensagemErro = e.message) }
            }
        }
    }

    // Explicação detalhada abaixo sobre este loop
    private fun iniciarLoopCombate() {
        jobCombate?.cancel()
        jobCombate = viewModelScope.launch {
            while (true) {
                delay(500)
                val currentState = _uiState.value
                val dps = currentState.dano

                val danoPorGolpe = (dps / 2f).coerceAtLeast(1f)
                var novaVidaMonstro = currentState.vidaMonstroAtual - danoPorGolpe

                // Ativa a animação de ataque
                _uiState.update { it.copy(animarAtaqueHeroi = true, logCombate = "Herói causou ${danoPorGolpe.toInt()} de dano em ${it.nomeMonstro}") }

                delay(100)
                _uiState.update { it.copy(animarAtaqueHeroi = false) }

                // Se o monstro morreu
                if (novaVidaMonstro <= 0f) {
                    ganharOuro(50)
                    val proximoNome = if (currentState.nomeMonstro == "Dragão de Cristal") "Rei Goblin Corrompido" else "Dragão de Cristal"
                    _uiState.update {
                        it.copy(
                            logCombate = "🎉 ${currentState.nomeMonstro} foi derrotado! +50 de Ouro.",
                            vidaMonstroAtual = it.vidaMonstroMax + 50f,
                            nomeMonstro = proximoNome
                        )
                    }
                    delay(1000)
                    novaVidaMonstro = _uiState.value.vidaMonstroMax
                } else {
                    _uiState.update { it.copy(vidaMonstroAtual = novaVidaMonstro) }
                }

                delay(600)
                // Dano do monstro no herói
                val novaVidaHeroi = _uiState.value.vidaHeroiAtual - 5f
                if (novaVidaHeroi <= 0f) {
                    _uiState.update {
                        it.copy(
                            logCombate = "⚠️ Herói foi derrotado! Recomeçando...",
                            vidaHeroiAtual = it.vidaHeroiMax
                        )
                    }
                    perderOuro(20)
                    delay(1500)
                } else {
                    _uiState.update { it.copy(vidaHeroiAtual = novaVidaHeroi) }
                }
            }
        }
    }

    fun ganharOuro(valor: Int) {
        val novoOuro = _uiState.value.ouro + valor
        _uiState.update { it.copy(ouro = novoOuro) }
        persistir()
    }

    fun perderOuro(valor: Int) {
        val novoOuro = (_uiState.value.ouro - valor).coerceAtLeast(0)
        _uiState.update { it.copy(ouro = novoOuro) }
        persistir()
    }

    private fun persistir() {
        viewModelScope.launch {
            val state = _uiState.value
            repository.salvarProgresso(
                ProgressoEntity(
                    ouro = state.ouro,
                    dano = state.dano,
                    skinEquipada = state.skinEquipada,
                    vidaMaxima = state.vidaHeroiMax.toInt(),
                    vidaAtual = state.vidaHeroiAtual.toInt()
                )
            )
        }
    }
}