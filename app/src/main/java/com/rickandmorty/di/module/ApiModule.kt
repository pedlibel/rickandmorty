package com.rickandmorty.di.module

import com.rickandmorty.data.db.ws.api.CharacterApi
import com.rickandmorty.data.db.ws.factory.RetrofitFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val apiModule = module {
    // single<RandomRepository>(named("name")) { RandomRepositoryImpl() }
    single(named("characterApi")) { RetrofitFactory(" https://rickandmortyapi.com").create() }
    factory { get<Retrofit>(named("characterApi")).create(CharacterApi::class.java) }
}
