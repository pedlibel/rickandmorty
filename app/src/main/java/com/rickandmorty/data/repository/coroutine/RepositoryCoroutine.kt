package com.rickandmorty.data.repository.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class RepositoryCoroutine(private val coroutineDispatchers: CoroutineDispatcher = Dispatchers.IO) {

    protected suspend fun <T> background(task: () -> T): T =
        withContext(coroutineDispatchers) {
            try {
                task.invoke()
            } catch (throwable: Throwable) {
                throw throwable
            }
        }
}
