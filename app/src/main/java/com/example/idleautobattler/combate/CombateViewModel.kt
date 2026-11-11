package com.example.idleautobattler.combate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CombateViewModel(
    private val repositorio: ProgressoRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(GameState())
    val estado: StateFlow<GameState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repositorio.carregar()?.let { _estado.value = it }

            while (isActive) {
                delay(Balanceamento.TICK_MS)

                val antes = _estado.value
                val depois = _estado.updateAndGet { aplicarTick(it) }

                if (depois.nivelMonstro != antes.nivelMonstro) {
                    repositorio.salvar(depois)
                }
            }
        }
    }
}