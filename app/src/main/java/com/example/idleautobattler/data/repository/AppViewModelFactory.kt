package com.example.idleautobattler.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.idleautobattler.telas.combate.CombateViewModel
import com.example.idleautobattler.telas.loja.LojaViewModel

class AppViewModelFactory(
    private val repository: JogoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CombateViewModel::class.java) -> {
                CombateViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LojaViewModel::class.java) -> {
                LojaViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
    }
}
