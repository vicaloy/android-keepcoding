package com.valoy.dragonball.presentation.game.fight

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valoy.dragonball.dto.CharacterDTO
import com.valoy.dragonball.presentation.common.Event
import com.valoy.dragonball.repository.Characters

class FightViewModel : ViewModel() {

    val stateLiveData: MutableLiveData<Event<State>> by lazy {
        MutableLiveData<Event<State>>()
    }

    fun onShowCharacters(selectedId: String, randomId: String) {
        val selectedCharacter = Characters.get().first { it.id == selectedId }
        val randomCharacter = Characters.get().first { it.id == randomId }


        if (selectedCharacter.power > 0 && randomCharacter.power > 0)
            stateLiveData.postValue(Event(State.Characters(selectedCharacter, randomCharacter)))
        else {
            val winner = listOf(selectedCharacter, randomCharacter).first { it.power > 0 }
            stateLiveData.postValue(Event(State.Winner(winner.name)))
        }
    }

    fun onFightCharacters(selectedId: String, randomId: String) {
        val decrease = listOf(10, 60).random()
        val characterId = listOf(selectedId, randomId).random()

        val character = Characters.get().first { it.id == characterId }

        val power = if (character.power - decrease < 0) 0 else character.power - decrease

        val characterKicked = CharacterDTO(
            name = character.name,
            favorite = character.favorite,
            id = character.id,
            description = character.description,
            photo = character.photo,
            power = power
        )

        Characters.save(characterKicked)

        onShowCharacters(selectedId, randomId)
    }


    sealed class State {
        data class Winner(val name: String) : State()
        data class Characters(val selected: CharacterDTO, val random: CharacterDTO) : State()
    }

}