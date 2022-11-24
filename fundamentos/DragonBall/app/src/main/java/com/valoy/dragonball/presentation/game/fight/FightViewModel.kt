package com.valoy.dragonball.presentation.game.fight

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valoy.dragonball.dto.CharacterDTO
import com.valoy.dragonball.repository.Characters

class FightViewModel : ViewModel() {


    val stateLiveData: MutableLiveData<State> by lazy {
        MutableLiveData<State>()
    }


    fun onShowCharacters(selectedId: String, randomId: String) {
        val selectedCharacter = Characters.get().first { it.id == selectedId }
        val randomCharacter = Characters.get().first { it.id == randomId }


        if (selectedCharacter.power > 0 && randomCharacter.power > 0)
            stateLiveData.postValue(State.Characters(selectedCharacter, randomCharacter))
        else {
            val winner = listOf(selectedCharacter, randomCharacter).first { it.power > 0 }
            stateLiveData.postValue(State.Winner(winner.name))
        }
    }

    fun onFightCharacters(selectedId: String, randomId: String) {
        val decrease = listOf(10, 60).random()
        val characterId = listOf(selectedId, randomId).random()

        val character = Characters.get().first { it.id == characterId }
        val characterKicked = CharacterDTO(
            name = character.name,
            favorite = character.favorite,
            id = character.id,
            description = character.description,
            photo = character.photo,
            power = character.power - decrease
        )

        Characters.save(characterKicked)

        onShowCharacters(selectedId, randomId)
    }


    sealed class State {
        data class Winner(val name: String) : State()
        data class Characters(val selected: CharacterDTO, val random: CharacterDTO) : State()
    }

}