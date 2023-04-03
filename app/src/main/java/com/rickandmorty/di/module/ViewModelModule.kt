package com.rickandmorty.di.module

import com.rickandmorty.presentation.features.character.details.CharacterDetailsViewModel
import com.rickandmorty.presentation.features.character.details.main.CharacterDetailsMainViewModel
import com.rickandmorty.presentation.features.character.list.CharacterListViewModel
import com.rickandmorty.presentation.features.character.list.main.CharacterListMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CharacterListViewModel() }
    viewModel { CharacterListMainViewModel(get()) }
    viewModel { CharacterDetailsViewModel() }
    viewModel { CharacterDetailsMainViewModel(get()) }
}
