package com.valoy.dragonball.repository

import com.valoy.dragonball.dto.CharacterDTO

object Characters {
    private val characters = mutableMapOf<String, CharacterDTO>()

    fun save(list: List<CharacterDTO>) {
        characters.putAll(list.associateBy { it.id })
    }

    fun save(character: CharacterDTO) {
        characters[character.id] = character
    }

    fun get(): List<CharacterDTO> {
        return characters.values.toList()
    }
}