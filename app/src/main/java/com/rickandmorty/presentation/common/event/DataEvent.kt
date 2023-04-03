package com.rickandmorty.presentation.common.event

import java.io.Serializable

class DataEvent<T>(
    val data: T,
    private val isSingleEvent: Boolean = true,
) : Serializable {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        if (!isSingleEvent) return data
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }
}
