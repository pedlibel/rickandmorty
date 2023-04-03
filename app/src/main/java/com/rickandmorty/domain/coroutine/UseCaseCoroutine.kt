package com.rickandmorty.domain.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCaseCoroutine(private val coroutineDispatchers: CoroutineDispatcher = Dispatchers.Default) {

    suspend fun <T> background(task: suspend () -> T): T {
        return withContext(coroutineDispatchers) {
            try {
                task()
            } catch (throwable: Throwable) {
                throw throwable
            }
        }
    }
}
