package com.rickandmorty.globalmocks

import com.rickandmorty.data.db.ws.model.response.CharacterResonse
import com.rickandmorty.entity.Character
import com.rickandmorty.entity.ServiceException

interface GlobalMocks {
    val mockCharacterResponse: CharacterResonse
    val mockCharacter: Character
    val mockCharacterResponseList: List<CharacterResonse>
    val mockCharacterList: List<Character>
    val mockServiceException: ServiceException
}
