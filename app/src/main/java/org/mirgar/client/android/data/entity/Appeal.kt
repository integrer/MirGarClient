package org.mirgar.client.android.data.entity

import androidx.room.*

@Entity(
    tableName = "appeals",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["appeal_category_id"]
    )],
    indices = [Index("is_own"), Index("appeal_category_id")]
)
data class Appeal(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "appeal_id") val id: Long = 0L,
    @ColumnInfo(name = "appeal_server_id") val serverId: Long? = null,
    @ColumnInfo(name = "appeal_title") var title: String? = null,
    @ColumnInfo(name = "is_own") val isOwn: Boolean = true,
    @ColumnInfo(name = "appeal_category_id") var categoryId: Long? = null
) {
    fun withId(id: Long) = Appeal(id, serverId, title, isOwn, categoryId)
}