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