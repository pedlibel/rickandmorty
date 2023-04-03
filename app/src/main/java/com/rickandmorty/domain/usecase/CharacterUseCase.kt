package com.rickandmorty.domain.usecase

import com.rickandmorty.entity.Character

interface CharacterUseCase {
    suspend fun getCharacterList(page: Int): List<Character>
    suspend fun updateCharacter(character: Character)
    suspend fun getCharacter(id: Int): Character
}
