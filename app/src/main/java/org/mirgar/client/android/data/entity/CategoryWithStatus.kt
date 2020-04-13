package org.mirgar.client.android.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class CategoryWithStatus(
    @Embedded val category: Category,
    val status: Boolean,
    @ColumnInfo(name = "appeal_id") val appealId: Long
)