package com.rickandmorty.domain.usecase.impl

import com.rickandmorty.data.repository.CharacterRepository
import com.rickandmorty.domain.coroutine.UseCaseCoroutine
import com.rickandmorty.domain.usecase.CharacterUseCase
import com.rickandmorty.entity.Character

class CharacterUseCaseImpl(private val characterRepository: CharacterRepository) :
    CharacterUseCase, UseCaseCoroutine() {
    override suspend fun getCharacterList(page: Int) =
        background { characterRepository.getCharacterList(page) }

    override suspend fun updateCharacter(character: Character) =
        background { characterRepository.updateCharacter(character) }

    override suspend fun getCharacter(id: Int) =
        background { characterRepository.getCharacter(id) }
}
