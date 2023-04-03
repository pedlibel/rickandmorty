package com.rickandmorty.presentation.base

import android.app.Application
import com.rickandmorty.di.module.apiModule
import com.rickandmorty.di.module.daoModule
import com.rickandmorty.di.module.repositoryModule
import com.rickandmorty.di.module.useCaseModule
import com.rickandmorty.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(apiModule, daoModule, repositoryModule, useCaseModule, viewModelModule)
        }
    }
}
