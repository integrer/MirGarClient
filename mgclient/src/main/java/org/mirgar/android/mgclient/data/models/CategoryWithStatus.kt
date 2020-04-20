package org.mirgar.android.mgclient.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import org.mirgar.android.mgclient.data.entity.Category

data class CategoryWithStatus(
    @Embedded val category: Category,
    @ColumnInfo(name = "is_related") val isRelated: Boolean,
    @ColumnInfo(name = "has_subcategories") val hasSubcategories: Boolean,
    @ColumnInfo(name = "appeal_id") val appealId: Long
)