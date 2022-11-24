package com.valoy.dragonball.presentation.game.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valoy.dragonball.dto.CharacterDTO
import com.valoy.dragonball.network.CharactersService
import com.valoy.dragonball.network.Resource
import com.valoy.dragonball.repository.Characters

class CharactersViewModel(private val charactersService: CharactersService) : ViewModel() {

    val stateLiveData: MutableLiveData<State> by lazy {
        MutableLiveData<State>()
    }

    init {
        getCharacters()
    }

    private fun getCharacters() {
        if (Characters.get().isEmpty()) {
            stateLiveData.postValue(State.Loading)
            charactersService.execute { resource ->
                if (resource is Resource.Success) {
                    resource.data?.let {
                        Characters.save(it)
                        stateLiveData.postValue(State.Success(it, searchWinner()))
                    }
                } else {
                    stateLiveData.postValue(State.Error("Try again"))
                }
            }
        } else {
            stateLiveData.postValue(State.Success(Characters.get(), searchWinner()))
        }
    }

    private fun searchWinner(): String? {
        val characters = Characters.get().filter { it.power > 0 }
        return if (characters.size == 1)
            characters.first().name
        else null

    }

    fun onCharacterClick(selected: CharacterDTO) {
        if (selected.power > 0) {
            val list = Characters.get().filter { it.id != selected.id && it.power > 0 }
            val random = list.random()

            stateLiveData.postValue(State.Fight(selected.id, random.id))
        }
    }

    sealed class State {
        data class Fight(val selectedId: String, val randomId: String) : State()
        data class Success(val characters: List<CharacterDTO>, val winner: String?) : State()
        data class Error(val message: String) : State()
        object Loading : State()
    }
}