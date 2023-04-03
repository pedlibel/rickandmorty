package com.rickandmorty.presentation.features.character.details

import com.rickandmorty.R
import com.rickandmorty.databinding.CharacterDetailsActivityBinding
import com.rickandmorty.presentation.common.base.BaseActivity

class CharacterDetailsActivity :
    BaseActivity<CharacterDetailsActivityBinding, CharacterDetailsViewModel>(
        R.layout.character_details_activity,
        CharacterDetailsViewModel::class.java
    ) {

    override fun bindView() {
        super.bindView()
        binding.vm = viewModel
    }

    override fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}
