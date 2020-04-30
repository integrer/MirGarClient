package org.mirgar.android.mgclient.data.dao

import androidx.room.*

import org.mirgar.android.mgclient.data.entity.RemoteAppealPhoto

@Dao
interface RemoteAppealPhotoDao {
    @Transaction
    suspend fun updateOrInsert(photo: RemoteAppealPhoto) {
        if (has(photo.id)) update(photo) else insert(photo)
    }

    @Update
    suspend fun update(photo: RemoteAppealPhoto)

    @Insert
    suspend fun insert(photo: RemoteAppealPhoto)

    @Query("""SELECT EXISTS(SELECT 1 FROM remote_appeal_photos
            WHERE remote_appeal_photo_appeal_id = :id LIMIT 1)""")
    suspend fun has(id: Long): Boolean
}