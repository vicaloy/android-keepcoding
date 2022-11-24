package com.valoy.dragonball.presentation.game.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valoy.dragonball.network.CharactersService

@Suppress("UNCHECKED_CAST")
class CharactersViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CharactersViewModel(CharactersService()) as T
}