package com.rickandmorty.di.module

import com.rickandmorty.data.repository.CharacterRepository
import com.rickandmorty.data.repository.impl.CharacterRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
}
