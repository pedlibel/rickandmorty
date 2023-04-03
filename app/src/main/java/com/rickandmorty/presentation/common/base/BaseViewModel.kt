@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.rickandmorty.presentation.common.base

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickandmorty.entity.ServiceException
import com.rickandmorty.presentation.common.event.DataEvent
import com.rickandmorty.presentation.common.extension.setDataEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {
    private var initialized = false
        get() =
            field.also { field = true }

    private val _ldServiceExceptionManager = MutableLiveData<DataEvent<ServiceException>>()
    val ldServiceExceptionManager: LiveData<DataEvent<ServiceException>> get() = _ldServiceExceptionManager

    private val _ldExceptionManager = MutableLiveData<DataEvent<Throwable>>()
    val ldExceptionManager: LiveData<DataEvent<Throwable>> get() = _ldExceptionManager

    private val coroutineDispatchers = Dispatchers.Main

    fun initViewModel(arguments: Bundle?, savedInstanceState: Bundle?) {
        if (!initialized) {
            init(arguments, savedInstanceState)
        } else {
            onReused()
        }
    }

    fun launchSuspendFunction(
        errorHandler: (Throwable) -> Unit = ::defaultErrorHandler,
        dispatcher: CoroutineDispatcher = coroutineDispatchers,
        suspendFunction: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            withContext(dispatcher) {
                try {
                    suspendFunction.invoke()
                } catch (throwable: Throwable) {
                    errorHandler(throwable)
                }
            }
        }
    }

    private fun defaultErrorHandler(throwable: Throwable) {
        when (throwable) {
            is ServiceException -> _ldServiceExceptionManager.setDataEvent(throwable)
            else -> _ldExceptionManager.setDataEvent(throwable)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    open fun init(arguments: Bundle?, savedInstanceState: Bundle?) {
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    open fun onReused() {
    }
}
