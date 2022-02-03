package org.mirgar.android.mgclient.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun intoAppealStatus(id: Int?) = id?.let { AppealStatus.from(it) }
    @TypeConverter
    fun fromAppealStatus(from: AppealStatus?) = from?.id
}