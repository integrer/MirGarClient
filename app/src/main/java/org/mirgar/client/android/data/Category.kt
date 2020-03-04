package org.mirgar.client.android.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        childColumns = ["super_id"],
        parentColumns = ["id"])]
)
data class Category(@PrimaryKey val id: Long, val title: String, @ColumnInfo(name = "super_id") val superId: Long?)
