package org.mirgar.client.android.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

class AppealWithCategoryTitle(
    @Embedded val appeal: Appeal,
    @ColumnInfo(name = "category_title") val categoryTitle: String?
)