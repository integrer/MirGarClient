package org.mirgar.android.common.data

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

