package com.rickandmorty.globalmocks.impl

import com.rickandmorty.data.db.ws.model.response.CharacterResonse
import com.rickandmorty.entity.Character
import com.rickandmorty.entity.ServiceException
import com.rickandmorty.globalmocks.GlobalMocks
import com.rickandmorty.presentation.common.makeRandomInstance

class GlobalMocksImpl : GlobalMocks {
    override val mockCharacterResponse by lazy { makeRandomInstance<CharacterResonse>() }
    override val mockCharacter by lazy { mockCharacterResponse.map() }
    override val mockCharacterResponseList by lazy { makeRandomInstance<List<CharacterResonse>>() }
    override val mockCharacterList by lazy { mockCharacterResponseList.map { innerMap -> innerMap.map() } }
    override val mockServiceException by lazy { makeRandomInstance<ServiceException>() }
}
