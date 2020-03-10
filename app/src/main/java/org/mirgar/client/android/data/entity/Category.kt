package org.mirgar.client.android.data.entity

import androidx.room.*

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["super_id"])],
    indices = [Index("super_id")]
)
data class Category(
    @ColumnInfo(name = "category_id") @PrimaryKey val id: Long,
    val title: String,
    @ColumnInfo(name = "super_id") val superId: Long?,
    @ColumnInfo(name = "icon_url") val iconUrl: String?,
    val checksum: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (title != other.title) return false
        if (superId != other.superId) return false
        if (iconUrl != other.iconUrl) return false
        if (!checksum.contentEquals(other.checksum)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (superId?.hashCode() ?: 0)
        result = 31 * result + (iconUrl?.hashCode() ?: 0)
        result = 31 * result + checksum.contentHashCode()
        return result
    }
}
