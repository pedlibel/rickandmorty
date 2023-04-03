package com.rickandmorty.presentation.features.character.details.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rickandmorty.domain.usecase.CharacterUseCase
import com.rickandmorty.entity.Character
import com.rickandmorty.presentation.common.base.BaseViewModel
import com.rickandmorty.presentation.common.event.DataEvent
import com.rickandmorty.presentation.common.extension.getValueOrDefault
import com.rickandmorty.presentation.common.extension.setDataEvent
import com.rickandmorty.presentation.features.character.details.CharacterDetailsActivityArgs
import java.lang.IllegalStateException

class CharacterDetailsMainViewModel(private val characterUseCase: CharacterUseCase) :
    BaseViewModel() {

    private val _ldOriginalCharacter = MutableLiveData<Character>()
    val ldOriginalCharacter: LiveData<Character> get() = _ldOriginalCharacter

    private val _ldSaveEvent = MutableLiveData<DataEvent<Boolean>>()
    val ldSaveEvent: LiveData<DataEvent<Boolean>> get() = _ldSaveEvent

    val ldCharacterName = MutableLiveData<String>()
    val ldCharacterGender = MutableLiveData<String>()
    val ldCharacterStatus = MutableLiveData<String>()

    override fun init(arguments: Bundle?, savedInstanceState: Bundle?) {
        super.init(arguments, savedInstanceState)
        arguments?.let { argumentsNotNull ->
            with(CharacterDetailsActivityArgs.fromBundle(argumentsNotNull)) {
                _ldOriginalCharacter.value = character.also {
                    ldCharacterName.value = it.name
                    ldCharacterGender.value = it.gender
                    ldCharacterStatus.value = it.status
                }
            }
        }
    }

    fun saveCharacter() {
        launchSuspendFunction {
            _ldOriginalCharacter.value?.copy(
                name = ldCharacterName.getValueOrDefault(""),
                gender = ldCharacterGender.getValueOrDefault(""),
                status = ldCharacterStatus.getValueOrDefault("")
            )?.let { modifiedCharacter ->
                characterUseCase.updateCharacter(modifiedCharacter)
                _ldSaveEvent.setDataEvent(true)
            }
        }
    }

    fun characterHasChanges(
        changedName: String?,
        changedGender: String?,
        changedStatus: String?,
    ) =
        with(_ldOriginalCharacter.value ?: throw IllegalStateException("Character not set")) {
            name != changedName || gender != changedGender || status != changedStatus
        }

    fun getCharacterId() =
        _ldOriginalCharacter.value?.id ?: throw IllegalStateException("Character not set")
}
