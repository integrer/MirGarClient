package org.mirgar.android.mgclient.data.entity

import androidx.room.*

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["super_id"])],
    indices = [Index("super_id"), Index("category_title")]
)
data class Category(
    @ColumnInfo(name = "category_id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "category_title") val title: String,
    @ColumnInfo(name = "super_id") val superId: Long?,
    @ColumnInfo(name = "icon_url") val iconUrl: String?)
