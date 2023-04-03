package com.rickandmorty.presentation.features.character.list.main

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.whenever
import com.rickandmorty.base.BaseUnitaryTest
import com.rickandmorty.domain.usecase.CharacterUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class CharacterListMainViewModelTest : BaseUnitaryTest() {

    private lateinit var viewModelTest: CharacterListMainViewModel

    @Mock
    private lateinit var characterUseCase: CharacterUseCase

    @Before
    override fun setUp() {
        super.setUp()
        viewModelTest = CharacterListMainViewModel(characterUseCase)
    }

    @Test
    fun `When the ViewModel init and call to getCharacterList service is called, then a list is set`() {
        runBlocking {
            /* Given */
            whenever(
                characterUseCase.getCharacterList(anyInt())
            ).thenAnswer {
                mockCharacterList
            }

            /* When */
            viewModelTest.init(mock(), mock())

            /* Then */
            liveDataAssertEquals(
                viewModelTest.ldCharacterList,
                mockCharacterList
            )
        }
    }

    @Test
    fun `When the ViewModel init and the call to getCharacterList service launches an error, then an error event is launched`() {
        runBlocking {
            /* Given */
            doAnswer {
                throw mockServiceException
            }.whenever(
                characterUseCase
            ).getCharacterList(anyInt())

            /* When */
            viewModelTest.init(mock(), mock())

            /* Then */
            liveDataAssertEquals(
                viewModelTest.ldServiceExceptionManager,
                mockServiceException
            )
        }
    }

    @Test
    fun `When a new page is requested, then a list is set`() {
        runBlocking {
            /* Given */
            whenever(
                characterUseCase.getCharacterList(anyInt())
            ).thenAnswer {
                mockCharacterList
            }

            /* When */
            viewModelTest.getCharactersNextPage()

            /* Then */
            liveDataAssertEquals(
                viewModelTest.ldCharacterList,
                mockCharacterList
            )
        }
    }

    @Test
    fun `When a new page is requested and the service launches an error, then an error event is launched`() {
        runBlocking {
            /* Given */
            doAnswer {
                throw mockServiceException
            }.whenever(
                characterUseCase
            ).getCharacterList(anyInt())

            /* When */
            viewModelTest.getCharactersNextPage()

            /* Then */
            liveDataAssertEquals(
                viewModelTest.ldServiceExceptionManager,
                mockServiceException
            )
        }
    }
}
