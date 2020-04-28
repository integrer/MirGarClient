package org.mirgar.android.mgclient.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.mirgar.android.mgclient.data.entity.AppealPhoto

@Dao
internal interface AppealPhotoDao {
    @Query("SELECT * FROM appeal_photos WHERE aphoto_appeal_id = :appealId")
    suspend fun ofAppeal(appealId: Long): List<AppealPhoto>

    @Transaction
    @Query("SELECT * FROM appeal_photos WHERE aphoto_appeal_id = :appealId")
    fun ofAppealAsLive(appealId: Long): LiveData<List<AppealPhoto>>

    @Insert
    suspend fun insert(appealPhoto: AppealPhoto)

    @Query("SELECT * FROM appeal_photos WHERE aphoto_id = :id LIMIT 1")
    fun getOneAsLive(id: Long): LiveData<AppealPhoto?>

    @Query("SELECT * FROM appeal_photos WHERE aphoto_id = :id LIMIT 1")
    suspend fun getOne(id: Long): AppealPhoto?

    @Delete
    suspend fun delete(appealPhoto: AppealPhoto)
}