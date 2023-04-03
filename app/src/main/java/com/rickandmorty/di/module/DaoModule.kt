package com.rickandmorty.di.module

import androidx.room.Room
import com.rickandmorty.data.db.ws.db.CharacterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val daoModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CharacterDatabase::class.java,
            "characters"
        ).build()
    }

    single { get<CharacterDatabase>().characterDao() }
}
