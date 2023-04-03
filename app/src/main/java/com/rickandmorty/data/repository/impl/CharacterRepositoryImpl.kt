package com.rickandmorty.data.repository.impl

import com.rickandmorty.data.db.ws.api.CharacterApi
import com.rickandmorty.data.db.ws.db.CharacterDao
import com.rickandmorty.data.db.ws.model.response.CharacterResonse
import com.rickandmorty.data.repository.CharacterRepository
import com.rickandmorty.data.repository.coroutine.RepositoryCoroutine
import com.rickandmorty.entity.Character
import com.rickandmorty.presentation.common.errormapper.ErrorMapper
import java.lang.IllegalStateException

class CharacterRepositoryImpl(val characterApi: CharacterApi, val characterDao: CharacterDao) :
    CharacterRepository,
    RepositoryCoroutine() {
    override suspend fun getCharacterList(page: Int): List<Character> =
        background {
            characterDao.getCharactersInRange(getInitialId(page), getFinalId(page)).ifEmpty {
                with(
                    characterApi.getCharactersFromPage(page).execute()
                ) {
                    body()?.result?.onEach { character ->
                        characterDao.insert(character)
                    } ?: throw ErrorMapper.errorHandler(this)
                }
            }.map { innerMap -> innerMap.map() }
        }

    override suspend fun updateCharacter(character: Character) {
        background {
            characterDao.update(
                CharacterResonse(
                    character.id,
                    character.name,
                    character.status,
                    character.species,
                    character.gender,
                    character.image,
                )
            )
        }
    }

    override suspend fun getCharacter(id: Int) =
        background {
            characterDao.getCharacterWithId(id)?.map()
                ?: throw IllegalStateException("Character ID not exits")
        }

    private fun getInitialId(page: Int) =
        1 + 20 * (page - 1)

    private fun getFinalId(page: Int) =
        20 * page
}
