package org.mirgar.android.common.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.*

open class AggregatedLiveData<S, R>(
    sources: Iterable<LiveData<out S>>,
    private val aggregator: (Sequence<S>) -> R
) : MediatorLiveData<R>() {
    private val values = WeakHashMap<LiveData<out S>, S>()

    private fun considerChange() {
        value = aggregator(values.values.asSequence())
    }

    init {
        sources.forEach { entry ->
            values[entry] = entry.value
            addSource(entry)
        }
        considerChange()
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        throw UnsupportedOperationException(
            "This method is not supported. Consider use overload " +
                    "with one parameter."
        )
    }

    open fun addSource(source: LiveData<out S>) {
        super.addSource(source) {
            values[source] = it
            considerChange()
        }
    }

    override fun <SS : Any?> removeSource(toRemote: LiveData<SS>) {
        values.remove(toRemote as LiveData<*>)
        super.removeSource(toRemote)
    }
}