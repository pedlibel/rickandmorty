package com.rickandmorty.presentation.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rickandmorty.entity.Character
import com.rickandmorty.entity.ScreenCodeEnum
import com.rickandmorty.presentation.common.base.BaseActivity
import com.rickandmorty.presentation.common.base.BaseFragment
import com.rickandmorty.presentation.common.event.DataEvent
import kotlin.properties.Delegates

fun BaseActivity<*, *>.getScreenCode(): ScreenCodeEnum =
    ScreenCodeEnum.screenToCode(this)

fun BaseFragment<*, *>.getScreenCode(): ScreenCodeEnum =
    ScreenCodeEnum.screenToCode(this)

fun ScreenCodeEnum.titleToCodeTitle(title: String) =
    "$code - $title"

inline fun <T1 : Any, T2 : Any, R : Any> whenAllNotNull(
    p1: T1?,
    p2: T2?,
    block: (T1, T2) -> R?,
): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> whenAllNotNull(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T : Any, R : Any> ignoreNull(vararg arguments: T?, block: (List<T>) -> R?): R? {
    return if (arguments.any { it != null }) {
        block(arguments.filterNotNull())
    } else null
}

inline fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.basicDiffUtil(
    initialValue: List<T>,
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new },
) =
    Delegates.observable(initialValue) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areContentsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size
        }).dispatchUpdatesTo(this@basicDiffUtil)
    }

fun <T> MutableLiveData<List<T>>.addCollection(listToAdd: List<T>) {
    value = value?.plus(listToAdd) ?: listToAdd
}

fun MutableLiveData<List<Character>>.modifyCharacter(characterToAdd: Character) {
    value?.toMutableList()?.let { valueCopy ->
        with(valueCopy.indexOfFirst { character -> character.id == characterToAdd.id }) {
            if (this != -1) {
                valueCopy[this] = characterToAdd
                value = valueCopy
            }
        }
    }
}

fun <T : Any?> LiveData<DataEvent<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    observer: (T) -> Unit,
) {
    this.observe(lifecycleOwner) { t -> notifyObserver(t.data, observer) }
}

private fun <T : Any?> notifyObserver(t: T, observer: (T) -> Unit) {
    if (t is DataEvent<*>) {
        t.getContentIfNotHandled()?.let {
            observer(t)
        }
    } else {
        observer(t)
    }
}

fun <T> MutableLiveData<DataEvent<T>>.setDataEvent(data: T) {
    value = DataEvent(data)
}

fun <T> MutableLiveData<DataEvent<T>>.postDataEvent(data: T) {
    postValue(DataEvent(data))
}

fun <T> LiveData<DataEvent<T>>.getDataEvent(data: T) {
    value?.data
}

fun <T> LiveData<T>.getValueOrDefault(default: T) =
    value ?: default

fun <T1, T2, T3, R> tripleCombineLiveData(
    liveData1: LiveData<T1>,
    liveData2: LiveData<T2>,
    liveData3: LiveData<T3>,
    combineFn: (value1: T1?, value2: T2?, value3: T3?) -> R,
): LiveData<R> = MediatorLiveData<R>().apply {
    addSource(liveData1) {
        value = combineFn(it, liveData2.value, liveData3.value)
    }
    addSource(liveData2) {
        value = combineFn(liveData1.value, it, liveData3.value)
    }
    addSource(liveData3) {
        value = combineFn(liveData1.value, liveData2.value, it)
    }
}
