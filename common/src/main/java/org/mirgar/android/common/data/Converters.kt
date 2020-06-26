package org.mirgar.android.common.data

import android.util.SparseArray
import androidx.core.util.getOrElse
import androidx.core.util.set
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun toLong(date: Calendar) = date.timeInMillis

    @TypeConverter
    fun toCalendar(timestamp: Long): Calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> Iterable<T>.tryMatchAsList(): List<T>? = when (this) {
    is List<T> -> this
    is Array<*> -> this.asList() as List<T>
    else -> null
}

fun <T> Iterable<T>.tryMatchAsListOrConvert(): List<T> = tryMatchAsList() ?: toList()

/**
 * Lazily initializes new list using sparse array under the hood
 */
fun <T, R> List<T>.lazyMap(transform: (T) -> R): List<R> = LazyMap(this, transform)

private class LazyMap<T, R>(
    private val input: List<T>,
    private val transform: (T) -> R
) : AbstractList<R>() {
    override val size = input.size

    val inner by lazy { SparseArray<R>(size) }

    override fun get(index: Int): R = inner.getOrElse(index) {
        transform(input[index]).also { inner[index] = it }
    }
}

