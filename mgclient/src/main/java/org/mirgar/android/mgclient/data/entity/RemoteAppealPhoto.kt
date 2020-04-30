package org.mirgar.android.mgclient.data.entity

import androidx.room.*

@Entity(
    tableName = "remote_appeal_photos",
    foreignKeys = [ForeignKey(
        entity = Appeal::class,
        parentColumns = ["appeal_id"],
        childColumns = ["remote_appeal_photo_appeal_id"]
    )],
    indices = [Index("remote_appeal_photo_appeal_id")]
)
data class RemoteAppealPhoto(
    @PrimaryKey @ColumnInfo(name = "remote_appeal_photo_id") val id: Long,
    @ColumnInfo(name = "remote_appeal_photo_appeal_id") val appealId: Long,
    @ColumnInfo(name = "remote_appeal_photo_related_path") val relatedPath: String
)