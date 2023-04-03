package com.rickandmorty.presentation.features.character.list.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rickandmorty.domain.usecase.CharacterUseCase
import com.rickandmorty.entity.Character
import com.rickandmorty.presentation.common.base.BaseViewModel
import com.rickandmorty.presentation.common.extension.addCollection
import com.rickandmorty.presentation.common.extension.modifyCharacter

class CharacterListMainViewModel(private val characterUseCase: CharacterUseCase) : BaseViewModel() {

    private var page = 1
    var paginationRefresh = 10
    var refreshing = false

    private val _ldCharacterList = MutableLiveData<List<Character>>()
    val ldCharacterList: LiveData<List<Character>> get() = _ldCharacterList

    override fun init(arguments: Bundle?, savedInstanceState: Bundle?) {
        super.init(arguments, savedInstanceState)
        getCharacters()
    }

    private fun getCharacters() {
        launchSuspendFunction {
            refreshing = true
            _ldCharacterList.addCollection(characterUseCase.getCharacterList(page++))
            refreshing = false
        }
    }

    fun getCharactersNextPage() {
        getCharacters()
    }

    fun getNavigationToCharacterDetails(character: Character) =
        CharacterListMainFragmentDirections.actionCharacterListMainFragmentToCharacterListActivity(
            character
        )

    fun reloadCharacter(id: Int) {
        launchSuspendFunction {
            _ldCharacterList.modifyCharacter(getCharacter(id))
        }
    }

    private suspend fun getCharacter(id: Int) =
        characterUseCase.getCharacter(id)
}
