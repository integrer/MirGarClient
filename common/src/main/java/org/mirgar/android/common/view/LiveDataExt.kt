package org.mirgar.android.common.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun<T> combine(vararg sources: LiveData<out T>): LiveData<T> {
    return combine(sources.asSequence())
}

fun<T> combine(sources: Sequence<LiveData<out T>>): LiveData<T> {
    val result = MediatorLiveData<T>()
    sources.forEach { result.addSource(it) { v -> result.value = v } }
    return result
}

fun<T> LiveData<out T>.combineWith(sources: Sequence<LiveData<out T>>): LiveData<T> {
    return combine(sources + this)
}

fun<T> LiveData<out T>.combineWith(vararg sources: LiveData<out T>): LiveData<T> {
    return combineWith(sources.asSequence())
}
