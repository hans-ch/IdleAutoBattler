package com.example.idleautobattler.telas.combate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idleautobattler.data.JogoRepository
import com.example.idleautobattler.data.ProgressoEntity
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

    init {
        carregarDadosLocaisERemotos()
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
                                skinEquipada = progresso.skinEquipada
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

        viewModelScope.launch {
            try {
                repository.buscarBonusServidor().onSuccess { dto ->
                    _uiState.update { it.copy(eventoBonusTexto = dto.eventoAtivo) }
                }.onFailure { erro ->
                    _uiState.update { it.copy(mensagemErro = "Falha de rede: ${erro.message}") }
                }
            } catch (_: Exception) {
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
                    skinEquipada = state.skinEquipada
                )
            )
        }
    }
}
