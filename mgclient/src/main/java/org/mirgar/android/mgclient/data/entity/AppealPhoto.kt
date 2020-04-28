package org.mirgar.android.mgclient.data.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = "appeal_photos",
    foreignKeys = [ForeignKey(
        entity = Appeal::class,
        parentColumns = ["appeal_id"],
        childColumns = ["aphoto_appeal_id"]
    )],
    indices = [Index("aphoto_appeal_id")]
)
data class AppealPhoto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "aphoto_id") val id: Long = 0L,
    @ColumnInfo(name = "aphoto_appeal_id") val appealId: Long,
    @ColumnInfo(name = "aphoto_filename") val fileName: String,
    @ColumnInfo(name = "aphoto_caption") val caption: String? = null,
    @ColumnInfo(name = "aphoto_created") val created: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "aphoto_ext") val ext: String = "jpg"
)