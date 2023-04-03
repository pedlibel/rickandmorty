package com.rickandmorty.presentation.features.character.list

import com.rickandmorty.R
import com.rickandmorty.databinding.CharacterListActivityBinding
import com.rickandmorty.presentation.common.base.BaseActivity

class CharacterListActivity : BaseActivity<CharacterListActivityBinding, CharacterListViewModel>(
    R.layout.character_list_activity,
    CharacterListViewModel::class.java
) {

    override fun bindView() {
        super.bindView()
        binding.vm = viewModel
    }

    override fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}
