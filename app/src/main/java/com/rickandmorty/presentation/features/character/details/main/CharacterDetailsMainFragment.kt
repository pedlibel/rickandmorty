package com.rickandmorty.presentation.features.character.details.main

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.rickandmorty.R
import com.rickandmorty.databinding.CharacterDetailsMainFragmentBinding
import com.rickandmorty.presentation.common.CHARACTER_ID
import com.rickandmorty.presentation.common.base.BaseFragment
import com.rickandmorty.presentation.common.extension.observeEvent
import com.rickandmorty.presentation.common.extension.tripleCombineLiveData

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharacterDetailsMainFragment :
    BaseFragment<CharacterDetailsMainFragmentBinding, CharacterDetailsMainViewModel>(
        R.layout.character_details_main_fragment,
        CharacterDetailsMainViewModel::class.java
    ) {

    override fun bindView() {
        super.bindView()
        binding.vm = viewModel
        initListeners()
        initObservers()
    }

    fun initListeners() {
        binding.mbSave.setOnClickListener {
            viewModel.saveCharacter()
        }
    }

    fun initObservers() {
        tripleCombineLiveData(
            viewModel.ldCharacterName,
            viewModel.ldCharacterGender,
            viewModel.ldCharacterStatus
        ) { name, gender, status -> characterHasChanges(name, gender, status) }.observe(
            viewLifecycleOwner
        ) {
            binding.mbSave.isEnabled = it
        }

        viewModel.ldSaveEvent.observeEvent(viewLifecycleOwner) {
            requireActivity().setResult(Activity.RESULT_OK, getIntentWithCharacterId())
            requireActivity().finish()
        }
    }

    private fun getIntentWithCharacterId() =
        Intent().putExtra(CHARACTER_ID, viewModel.getCharacterId())

    private fun characterHasChanges(name: String?, gender: String?, status: String?) =
        viewModel.characterHasChanges(name, gender, status)

    override fun updateToolbar() {
        setToolbarTitle(R.string.character_details_title)
    }
}
