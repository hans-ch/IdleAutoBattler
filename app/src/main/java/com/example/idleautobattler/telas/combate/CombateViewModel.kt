package com.example.idleautobattler.telas.combate

import androidx.compose.runtime.Composable
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

    private fun iniciarLoopCombate() {
        jobCombate?.cancel()
        jobCombate = viewModelScope.launch {
            while (true) {
                delay(500)
                val currentState = _uiState.value
                val dps = currentState.dano

                val danoPorGolpe = (dps / 2f).coerceAtLeast(1f)
                var novaVidaMonstro = currentState.vidaMonstroAtual - danoPorGolpe

                // Animação de ataque do herói
                _uiState.update { it.copy(animarAtaqueHeroi = true, logCombate = "Herói causou ${danoPorGolpe.toInt()} de dano em ${it.nomeMonstro}") }

                delay(100)
                _uiState.update { it.copy(animarAtaqueHeroi = false) }

                // Se o monstro foi derrotado
                if (novaVidaMonstro <= 0f) {
                    val novosMonstrosDerrotados = currentState.monstrosDerrotadosNaFase + 1
                    val recompensaOuro = 50 * currentState.faseAtual
                    ganharOuro(recompensaOuro)

                    if (novosMonstrosDerrotados >= currentState.monstrosNecessariosParaProximaFase) {
                        // Avança de fase
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
                        // Apenas troca de monstro na mesma fase
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
                // Dano do monstro no herói com animação de ataque
                _uiState.update { it.copy(animarAtaqueMonstro = true) }
                delay(150) // Duração rápida da animação do monstro
                _uiState.update { it.copy(animarAtaqueMonstro = false) }

                val danoMonstro = 5f * currentState.faseAtual
                val novaVidaHeroi = _uiState.value.vidaHeroiAtual - danoMonstro

                if (novaVidaHeroi <= 0f) {
                    // O Herói morreu: penalidade de ouro e restauração da vida, mantendo a fase atual!
                    perderOuro(500)

                    _uiState.update {
                        it.copy(
                            logCombate = "⚠️ Herói foi derrotado na Fase ${currentState.faseAtual}! Revivendo...",
                            vidaHeroiAtual = it.vidaHeroiMax,
                            // Opcional: Se quiser que o monstro recupere toda a vida ao matar o herói:
                            vidaMonstroAtual = it.vidaMonstroMax
                        )
                    }
                    delay(1500) // Pausa para o jogador ler o log antes de continuar
                } else {
                    _uiState.update { it.copy(vidaHeroiAtual = novaVidaHeroi) }
                }
            }
        }
    }

    // Função auxiliar para definir monstros diferentes conforme a fase avança
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
        persistir() // Salva automaticamente no banco de dados Room
    }

    fun desferirGolpeEspecial() {
        val currentState = _uiState.value
        if (!currentState.podeAtacarEspecial) return

        val danoEspecial = currentState.dano * 2 // Causa 2x o dano atual
        val novaVidaMonstro = currentState.vidaMonstroAtual - danoEspecial

        _uiState.update {
            it.copy(
                vidaMonstroAtual = novaVidaMonstro.coerceAtLeast(0f),
                logCombate = "⚡ GOLPE FÚRIA! Herói causou $danoEspecial de dano crítico!",
                podeAtacarEspecial = false
            )
        }

        // Cooldown de 5 segundos para usar o especial novamente
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