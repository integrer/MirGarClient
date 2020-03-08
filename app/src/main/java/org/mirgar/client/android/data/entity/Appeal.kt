package org.mirgar.client.android.data.entity

import androidx.room.*

@Entity(
    tableName = "appeals",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"]
    )],
    indices = [Index("is_own")]
)
class Appeal(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "is_own") val isOwn: Boolean,
    @ColumnInfo(name = "category_id") val categoryId: Long?,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val category: Category?
)