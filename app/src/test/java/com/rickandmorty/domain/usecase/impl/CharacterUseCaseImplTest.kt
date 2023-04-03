package com.rickandmorty.domain.usecase.impl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.rickandmorty.base.BaseUnitaryTest
import com.rickandmorty.data.repository.CharacterRepository
import com.rickandmorty.domain.usecase.CharacterUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock

@ExperimentalCoroutinesApi
class CharacterUseCaseImplTest : BaseUnitaryTest() {

    private lateinit var useCaseTest: CharacterUseCase

    @Mock
    private lateinit var characterRepository: CharacterRepository

    @Before
    override fun setUp() {
        super.setUp()
        useCaseTest = CharacterUseCaseImpl(characterRepository)
    }

    @Test
    fun `When a getCharacterList service is called, then a list is returned`() {
        runBlocking {
            /* Given */
            whenever(
                characterRepository.getCharacterList(any())
            ).thenAnswer {
                mockCharacterList
            }

            /* When */
            val response = useCaseTest.getCharacterList(1)

            /* Then */
            assert(
                response == mockCharacterList
            )
        }
    }

    @Test
    fun `When a getCharacter service is called, then a list is returned`() {
        runBlocking {
            /* Given */
            whenever(
                characterRepository.getCharacter(anyInt())
            ).thenAnswer {
                mockCharacter
            }

            /* When */
            val response = useCaseTest.getCharacter(1)

            /* Then */
            assert(
                response == mockCharacter
            )
        }
    }

    @Test
    fun `When a updateCharacter service is called, then a list is returned`() {
        runBlocking {
            /* Given */
            whenever(
                characterRepository.updateCharacter(any())
            ).thenAnswer { }

            /* When */
            val response = useCaseTest.updateCharacter(mockCharacter)

            /* Then */
            assert(
                response == Unit
            )
        }
    }
}
