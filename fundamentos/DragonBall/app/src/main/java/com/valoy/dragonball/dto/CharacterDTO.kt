package com.valoy.dragonball.dto

import com.google.gson.Gson

data class CharacterDTO(
    val name: String,
    val favorite: Boolean,
    val id: String,
    val description: String,
    val photo: String,
    val power: Int = 0
) {
    companion object {
        fun listFromJSON(json: String): List<CharacterDTO> {
            return Gson().fromJson(json, Array<CharacterDTO>::class.java)
                .map {
                    it.copy(
                        name = it.name,
                        favorite = it.favorite,
                        id = it.id,
                        description = it.description,
                        photo = it.photo,
                        power = 100
                    )
                }

        }
    }
}