package com.valoy.dragonball.presentation.game.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valoy.dragonball.dto.CharacterDTO
import com.valoy.dragonball.network.CharactersService
import com.valoy.dragonball.network.Resource
import com.valoy.dragonball.presentation.common.Event
import com.valoy.dragonball.repository.Characters

class CharactersViewModel(private val charactersService: CharactersService) : ViewModel() {

    val stateLiveData: MutableLiveData<Event<State>> by lazy {
        MutableLiveData<Event<State>>()
    }

    init {
        getCharacters()
    }

    fun onCharacterClick(selected: CharacterDTO) {
        if (selected.power > 0) {
            val list = Characters.get().filter { it.id != selected.id && it.power > 0 }
            if (list.isNotEmpty()) {
                val random = list.random()
                stateLiveData.postValue(Event(State.Fight(selected.id, random.id)))
            }
        }
    }

    private fun getCharacters() {
        if (Characters.get().isEmpty()) {
            stateLiveData.postValue(Event(State.Loading))
            charactersService.execute { resource ->
                if (resource is Resource.Success) {
                    resource.data?.let {
                        Characters.save(it)
                        stateLiveData.postValue(Event(State.Success(it, searchWinner())))
                    }
                } else {
                    stateLiveData.postValue(Event(State.Error("Try again")))
                }
            }
        } else {
            stateLiveData.postValue(Event(State.Success(Characters.get(), searchWinner())))
        }
    }

    private fun searchWinner(): String? {
        val characters = Characters.get().filter { it.power > 0 }
        return if (characters.size == 1)
            characters.first().name
        else null

    }


    sealed class State {
        data class Fight(val selectedId: String, val randomId: String) : State()
        data class Success(val characters: List<CharacterDTO>, val winner: String?) : State()
        data class Error(val message: String) : State()
        object Loading : State()
    }
}