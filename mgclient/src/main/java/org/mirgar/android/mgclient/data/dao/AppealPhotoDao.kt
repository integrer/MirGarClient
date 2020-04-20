package org.mirgar.android.mgclient.data.dao

import androidx.room.Dao
import androidx.room.Query
import org.mirgar.android.mgclient.data.entity.AppealPhoto

@Dao
interface AppealPhotoDao {
    @Query("SELECT * FROM appeal_photos WHERE aphoto_appeal_id = :appealId")
    suspend fun ofAppeal(appealId: Long): List<AppealPhoto>
}