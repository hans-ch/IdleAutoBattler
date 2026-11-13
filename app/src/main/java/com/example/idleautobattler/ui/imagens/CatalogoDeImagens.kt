package com.example.idleautobattler.ui.imagens

import androidx.annotation.DrawableRes
import com.example.idleautobattler.R

object CatalogoDeImagens {

    // --- Inimigos ---
    @DrawableRes val monstroSlime = R.drawable.monstro_slime
    @DrawableRes val monstroDiabrete = R.drawable.monstro_diabrete
    @DrawableRes val monstroCaveira = R.drawable.monstro_caveira
    @DrawableRes val monstroFantasma = R.drawable.monstro_fantasma

    // --- Guerreiro / skins ---
    @DrawableRes val guerreiro = R.drawable.guerreiro
    @DrawableRes val magoFogo = R.drawable.mago_fogo
    @DrawableRes val matadorDragao = R.drawable.matador_dragao
    @DrawableRes val espadachimCyborg = R.drawable.espadachim_cyborg
    @DrawableRes val ninja = R.drawable.ninja
    @DrawableRes val reiSupremo = R.drawable.rei_supremo




    @DrawableRes
    fun iconeDeMonstro(nome: String): Int = when (nome) {
        "Slime de Cristal" -> monstroSlime
        "Diabrete Corrompido" -> monstroDiabrete
        "Rei Esqueleto" -> monstroCaveira
        "Chefão Fantasma das Sombras" -> monstroFantasma
        else -> monstroFantasma
    }

    /**
     * Arte da skin equipada.
     *
     * Fase 1: apenas "Heroi Padrao" tem arte pronta.
     * Fase 2: as demais entram aqui quando os PNGs chegarem.
     * null = ainda sem arte, a UI usa o emoji como fallback.
     */
    @DrawableRes
    fun iconeDeSkin(nome: String): Int? = when (nome) {
        "Herói Padrão" -> guerreiro
        "Mago de Fogo" -> magoFogo
        "Matador de Dragões" -> matadorDragao
        "Espadachim Cyborg" -> espadachimCyborg
        "Ninja" -> ninja
        "Rei Supremo" -> reiSupremo
        else -> null
    }
}
