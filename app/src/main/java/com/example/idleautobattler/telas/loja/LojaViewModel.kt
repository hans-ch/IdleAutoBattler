package com.example.idleautobattler.telas.loja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idleautobattler.data.ItemLojaEntity
import com.example.idleautobattler.data.JogoRepository
import com.example.idleautobattler.data.ProgressoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LojaViewModel(
    private val repository: JogoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LojaUiState())
    val uiState: StateFlow<LojaUiState> = _uiState.asStateFlow()

    private var todosItensDoBanco: List<ItemLojaEntity> = emptyList()
    private var estaPopulandoIniciais = false

    init {
        observarDados()
    }

    private fun observarDados() {
        viewModelScope.launch {
            try {
                repository.obterProgressoFlow().collect { progresso ->
                    if (progresso != null) {
                        _uiState.update {
                            it.copy(ouroJogador = progresso.ouro, danoJogador = progresso.dano)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensagemErro = e.message) }
            }
        }

        viewModelScope.launch {
            try {
                repository.obterItensLojaFlow().collect { listaItens ->
                    todosItensDoBanco = listaItens
                    if (listaItens.isEmpty() && !estaPopulandoIniciais) {
                        estaPopulandoIniciais = true
                        popularItensIniciais()
                    } else {
                        aplicarFiltroEOrdenacao()
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensagemErro = e.message) }
            }
        }
    }

    private suspend fun popularItensIniciais() {
        val itensIniciais = listOf(
            ItemLojaEntity(titulo = "Aumentar Dano (+5 DPS)", nivelAtual = 1, precoAtual = 10, tipo = "DANO", aumentoDano = 5),
            ItemLojaEntity(titulo = "Espada Afiada (+25 DPS)", nivelAtual = 0, precoAtual = 50, tipo = "DANO", aumentoDano = 25),
            ItemLojaEntity(titulo = "Fúria do Guerreiro (+100 DPS)", nivelAtual = 0, precoAtual = 200, tipo = "DANO", aumentoDano = 100),
            ItemLojaEntity(titulo = "Aumentar Vida Máxima (+50 HP)", nivelAtual = 0, precoAtual = 20, tipo = "VIDA", aumentoDano = 0)
        )
        itensIniciais.forEach { repository.adicionarItemLoja(it) }
    }

    private fun aplicarFiltroEOrdenacao() {
        var lista = todosItensDoBanco

        if (_uiState.value.filtroTipo != "TODOS") {
            lista = lista.filter { item -> item.tipo == _uiState.value.filtroTipo }
        }

        if (_uiState.value.ordenarPorPreco) {
            lista = lista.sortedBy { item -> item.precoAtual }
        } else {
            lista = lista.sortedByDescending { item -> item.nivelAtual }
        }

        _uiState.update { it.copy(itensExibidos = lista) }
    }

    fun alterarFiltro(tipo: String) {
        _uiState.update { it.copy(filtroTipo = tipo) }
        aplicarFiltroEOrdenacao()
    }

    fun alternarOrdenacao() {
        _uiState.update { it.copy(ordenarPorPreco = !_uiState.value.ordenarPorPreco) }
        aplicarFiltroEOrdenacao()
    }

    fun alternarModoAdmin() {
        _uiState.update { it.copy(ehAdmin = !_uiState.value.ehAdmin) }
    }

    fun criarNovoItem(titulo: String, preco: Int, tipo: String, aumentoDano: Int) {
        viewModelScope.launch {
            val novoItem = ItemLojaEntity(
                titulo = titulo,
                nivelAtual = 0,
                precoAtual = preco,
                tipo = tipo,
                aumentoDano = aumentoDano
            )
            repository.adicionarItemLoja(novoItem)
        }
    }

    fun comprarUpgrade(item: ItemLojaEntity) {
        viewModelScope.launch {
            val estado = _uiState.value
            if (estado.ouroJogador >= item.precoAtual) {
                val novoOuro = estado.ouroJogador - item.precoAtual
                val novoDano = estado.danoJogador + item.aumentoDano

                repository.salvarProgresso(
                    ProgressoEntity(ouro = novoOuro, dano = novoDano)
                )

                val itemAtualizado = item.copy(
                    nivelAtual = item.nivelAtual + 1,
                    precoAtual = (item.precoAtual * 1.5).toInt() + 5
                )
                repository.atualizarItemLoja(itemAtualizado)
            } else {
                _uiState.update { it.copy(mensagemErro = "Ouro insuficiente!") }
            }
        }
    }

    fun deletarItem(item: ItemLojaEntity) {
        viewModelScope.launch {
            repository.deletarItemLoja(item)
        }
    }
}
