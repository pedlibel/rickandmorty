package com.rickandmorty.di.module

import com.rickandmorty.domain.usecase.CharacterUseCase
import com.rickandmorty.domain.usecase.impl.CharacterUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<CharacterUseCase> { CharacterUseCaseImpl(get()) }
}
