package org.mirgar.android.mgclient.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun intoAppealStatus(id: Int) = AppealStatus.from(id)
    @TypeConverter fun fromAppealStatus(from: AppealStatus) = from.id
}