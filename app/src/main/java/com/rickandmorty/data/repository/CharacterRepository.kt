package com.rickandmorty.data.repository

import com.rickandmorty.entity.Character

interface CharacterRepository {
    suspend fun getCharacterList(page: Int): List<Character>
    suspend fun updateCharacter(character: Character)
    suspend fun getCharacter(id: Int): Character
}
