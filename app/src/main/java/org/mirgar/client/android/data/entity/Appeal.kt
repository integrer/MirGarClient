package org.mirgar.client.android.data.entity

import androidx.room.*

@Entity(
    tableName = "appeals",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["_category_id"]
    )],
    indices = [Index("is_own"), Index("_category_id")]
)
data class Appeal(
    @PrimaryKey @ColumnInfo(name = "appeal_id") val id: Long,
    @ColumnInfo(name = "is_own") val isOwn: Boolean,
    @ColumnInfo(name = "_category_id") val categoryId: Long?
)