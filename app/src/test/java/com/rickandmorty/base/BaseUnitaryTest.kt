package com.rickandmorty.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.rickandmorty.globalmocks.GlobalMocks
import com.rickandmorty.globalmocks.impl.GlobalMocksImpl
import com.rickandmorty.presentation.common.event.DataEvent
import org.junit.Before
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

abstract class BaseUnitaryTest(val globalMocksImpl: GlobalMocksImpl = GlobalMocksImpl()) :
    GlobalMocks by globalMocksImpl {

    @Before
    open fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    inline fun <reified T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {},
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)

        try {
            afterObserve.invoke()

            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }
        } finally {
            this.removeObserver(observer)
        }

        return data as T
    }

    @JvmName("getOrAwaitValueOnlyData")
    inline fun <reified T> LiveData<T>.getOrAwaitValueOnlyData(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {},
    ): T {
        return getOrAwaitValue(time, timeUnit, afterObserve)
    }

    @JvmName("getOrAwaitValueOnlyDataMGAAppEvent")
    fun <T> LiveData<DataEvent<T>>.getOrAwaitValueOnlyData(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {},
    ): T {
        return getOrAwaitValue(time, timeUnit, afterObserve).data
    }

    @JvmName("liveDataAssertEquals")
    inline fun <reified T> liveDataAssertEquals(
        liveData: LiveData<T>,
        value: T?,
        noinline predicate: ((T, T?) -> Boolean)? = null,
    ) {
        val ldValue =
            liveData.getOrAwaitValueOnlyData()
        assert(
            when {
                predicate != null -> {
                    predicate.invoke(ldValue, value)
                }

                else -> ldValue == value
            }
        )
    }

    @JvmName("liveDataAssertEqualsMGAAppEvent")
    fun <T> liveDataAssertEquals(
        liveData: LiveData<DataEvent<T>>,
        value: T?,
        predicate: ((T, T?) -> Boolean)? = null,
    ) {
        val ldValue =
            liveData.getOrAwaitValueOnlyData()
        assert(
            when {
                predicate != null -> {
                    predicate.invoke(ldValue, value)
                }

                else -> ldValue == value
            }
        )
    }
}
