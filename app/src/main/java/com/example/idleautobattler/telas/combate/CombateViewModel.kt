package com.example.idleautobattler.telas.combate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idleautobattler.data.JogoRepository
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
        iniciarLoopCombate()
    }

    private fun carregarDadosLocaisERemotos() {
        viewModelScope.launch {
            try {
                val resultadoBonus = repository.buscarBonusServidor()
                resultadoBonus.onSuccess { bonus ->
                    _uiState.update { it.copy(eventoBonusTexto = bonus.eventoAtivo) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensagemErro = e.message) }
            }
        }

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

    private fun iniciarLoopCombate() {
        jobCombate?.cancel()
        jobCombate = viewModelScope.launch {
            while (true) {
                delay(500)
                val currentState = _uiState.value
                val dps = currentState.dano

                val danoPorGolpe = (dps / 2f).coerceAtLeast(1f)
                val novaVidaMonstro = currentState.vidaMonstroAtual - danoPorGolpe

                _uiState.update { it.copy(animarAtaqueHeroi = true, logCombate = "Herói causou ${danoPorGolpe.toInt()} de dano em ${it.nomeMonstro}") }

                delay(100)
                _uiState.update { it.copy(animarAtaqueHeroi = false) }

                if (novaVidaMonstro <= 0f) {
                    val novosMonstrosDerrotados = currentState.monstrosDerrotadosNaFase + 1
                    val recompensaOuro = 100 * currentState.faseAtual
                    ganharOuro(recompensaOuro)

                    if (novosMonstrosDerrotados >= currentState.monstrosNecessariosParaProximaFase) {
                        val proximaFase = currentState.faseAtual + 1
                        val novaVidaMax = 200f + (proximaFase * 50f)
                        val proximoNome = obterNomeMonstroPorFase(proximaFase)

                        _uiState.update {
                            it.copy(
                                faseAtual = proximaFase,
                                monstrosDerrotadosNaFase = 0,
                                nomeMonstro = proximoNome,
                                vidaMonstroMax = novaVidaMax,
                                vidaMonstroAtual = novaVidaMax,
                                logCombate = "🏆 Fase ${currentState.faseAtual} concluída! Bem-vindo à Fase $proximaFase!"
                            )
                        }
                    } else {
                        val proximoNome = if (currentState.nomeMonstro == "Dragão de Cristal") "Rei Goblin Corrompido" else "Dragão de Cristal"
                        val vidaMaxAtual = currentState.vidaMonstroMax
                        _uiState.update {
                            it.copy(
                                monstrosDerrotadosNaFase = novosMonstrosDerrotados,
                                logCombate = "🎉 ${currentState.nomeMonstro} derrotado! +$recompensaOuro de Ouro ($novosMonstrosDerrotados/${currentState.monstrosNecessariosParaProximaFase})",
                                vidaMonstroAtual = vidaMaxAtual,
                                nomeMonstro = proximoNome
                            )
                        }
                    }
                    delay(1000)
                } else {
                    _uiState.update { it.copy(vidaMonstroAtual = novaVidaMonstro) }
                }

                delay(600)
                _uiState.update { it.copy(animarAtaqueMonstro = true) }
                delay(150)
                _uiState.update { it.copy(animarAtaqueMonstro = false) }

                val danoMonstro = 5f * currentState.faseAtual
                val novaVidaHeroi = _uiState.value.vidaHeroiAtual - danoMonstro

                if (novaVidaHeroi <= 0f) {
                    perderOuro(150)

                    _uiState.update {
                        it.copy(
                            logCombate = "⚠️ Herói foi derrotado na Fase ${currentState.faseAtual}! Revivendo...",
                            vidaHeroiAtual = it.vidaHeroiMax,
                            vidaMonstroAtual = it.vidaMonstroMax
                        )
                    }
                    delay(1500)
                } else {
                    _uiState.update { it.copy(vidaHeroiAtual = novaVidaHeroi) }
                }
            }
        }
    }

    private fun obterNomeMonstroPorFase(fase: Int): String {
        return when (fase % 3) {
            1 -> "Dragão de Cristal"
            2 -> "Rei Goblin Corrompido"
            else -> "Chefão Titã das Sombras"
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

    fun equiparSkin(novaSkin: String) {
        _uiState.update { it.copy(skinEquipada = novaSkin) }
        persistir()
    }

    fun desferirGolpeEspecial() {
        val currentState = _uiState.value
        if (!currentState.podeAtacarEspecial) return

        val danoEspecial = currentState.dano * 2
        val novaVidaMonstro = currentState.vidaMonstroAtual - danoEspecial

        _uiState.update {
            it.copy(
                vidaMonstroAtual = novaVidaMonstro.coerceAtLeast(0f),
                logCombate = "⚡ GOLPE FÚRIA! Herói causou $danoEspecial de dano crítico!",
                podeAtacarEspecial = false
            )
        }

        viewModelScope.launch {
            delay(5000)
            _uiState.update { it.copy(podeAtacarEspecial = true) }
        }
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
