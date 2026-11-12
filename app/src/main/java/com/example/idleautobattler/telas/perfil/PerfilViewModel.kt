package com.example.idleautobattler.telas.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idleautobattler.telas.combate.ProgressoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PerfilUiState(
    val progresso: ProgressoEntity? = null,
    val skins: List<SkinEntity> = emptyList(),
    val skinEquipada: SkinEntity? = null
) {
    val nome: String
        get() = "Guerreiro"

    val ouro: Int
        get() = progresso?.ouro ?: 0
}

class PerfilViewModel(
    private val repository: PerfilRepository
) : ViewModel() {

    val uiState: StateFlow<PerfilUiState> =
        combine(
            repository.progresso,
            repository.skins,
            repository.skinEquipada
        ) { progresso, skins, skinEquipada ->
            PerfilUiState(
                progresso = progresso,
                skins = skins,
                skinEquipada = skinEquipada
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PerfilUiState()
        )

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem

    fun inicializar(nomeInicial: String) {
        viewModelScope.launch {
            repository.inicializar(nomeInicial)
        }
    }

    fun comprarOuEquipar(skin: SkinEntity) {
        viewModelScope.launch {
            if (skin.desbloqueada) {
                val equipada = repository.equiparSkin(skin)
                _mensagem.value = if (equipada) "Skin equipada." else "Não foi possível equipar essa skin."
            } else {
                val comprada = repository.comprarSkin(skin)
                _mensagem.value = if (comprada) "Skin desbloqueada." else "Ouro insuficiente."
            }
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}
