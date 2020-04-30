package org.mirgar.android.mgclient.data.entity

import androidx.room.*

import org.mirgar.android.mgclient.data.AppealStatus
import org.mirgar.android.mgclient.data.net.models.AppealIn as NetAppealIn

@Entity(
    tableName = "appeals",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["appeal_category_id"]
    )],
    indices = [
        Index("appeal_remote_id"),
        Index("is_own"),
        Index("appeal_category_id"),
        Index("appeal_status")
    ]
)
data class Appeal(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "appeal_id") val id: Long = 0L,
    @ColumnInfo(name = "appeal_remote_id") val remoteId: Long? = null,
    @ColumnInfo(name = "appeal_title") val title: String = "",
    @ColumnInfo(name = "appeal_description") val description: String = "",
    @ColumnInfo(name = "is_own") val isOwn: Boolean = true,
    @ColumnInfo(name = "appeal_status") val status: AppealStatus? = null,
    @ColumnInfo(name = "appeal_longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "appeal_latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "appeal_category_id") val categoryId: Long? = null,
    @ColumnInfo(name = "appeal_user_id") val userId: Long? = null
) {
    fun apply(netAppeal: NetAppealIn) = this.copy(
        remoteId = netAppeal.id.toLong(),
        title = netAppeal.name,
        description = netAppeal.description ?: "",
        status = netAppeal.type_id?.let { AppealStatus.from(it) },
        latitude = netAppeal.latitude ?: 0.0,
        longitude = netAppeal.longitude ?: 0.0,
        categoryId = netAppeal.cat_id.takeIf { it != 0 }?.toLong(),
        userId = netAppeal.user_id?.toLong()
    )

    companion object {
        val default = Appeal()
    }
}