package org.mirgar.client.android.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "appeals",
    indices = [Index("is_own")]
)
class Appeal (
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "is_own") var isOwn: Boolean
)