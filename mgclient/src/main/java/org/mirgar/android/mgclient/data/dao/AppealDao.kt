package org.mirgar.android.mgclient.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.mirgar.android.mgclient.data.entity.Appeal
import org.mirgar.android.mgclient.data.models.AppealPreview

@Dao
internal interface AppealDao {
    @Transaction
    @Query("""SELECT appeals.*, category_title
        FROM appeals LEFT JOIN categories ON category_id = appeal_category_id
        WHERE appeal_remote_id IS NULL""")
    fun getOwnWithCategoryTitle(): LiveData<List<AppealPreview>>

    @Transaction
    @Query("""SELECT appeals.*, category_title
        FROM appeals LEFT JOIN categories ON category_id = appeal_category_id
        WHERE appeal_remote_id IS NULL OR appeal_user_id = :userId""")
    fun getOwnWithCategoryTitle(userId: Long): LiveData<List<AppealPreview>>

    @Transaction
    @Query("""SELECT appeals.*, category_title
        FROM appeals LEFT JOIN categories ON category_id = appeal_category_id
        WHERE appeal_remote_id IS NOT NULL""")
    fun getAllWithCategoryTitle(): LiveData<List<AppealPreview>>

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    suspend fun getById(id: Long): Appeal?

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    fun getLiveById(id: Long): LiveData<Appeal>

    @Transaction
    @Query("""SELECT appeals.*, category_title
        FROM appeals LEFT JOIN categories ON category_id = appeal_category_id
        WHERE appeal_id = :id LIMIT 1""")
    fun getPreviewLiveById(id: Long): LiveData<AppealPreview>

    @Query("SELECT EXISTS(SELECT 1 FROM appeals WHERE appeal_id = :id LIMIT 1)")
    suspend fun has(id: Long): Boolean

    @Insert
    suspend fun insert(appeal: Appeal): Long

    @Update
    suspend fun update(appeal: Appeal)

    @Query("UPDATE appeals SET appeal_category_id = :categoryId WHERE appeal_id = :id")
    suspend fun setCategory(id: Long, categoryId: Long?)

    @Query("SELECT appeal_category_id FROM appeals WHERE appeal_id = :id LIMIT 1")
    suspend fun getCategoryId(id: Long): Long?

    @Query("DELETE FROM appeals WHERE appeal_id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM appeals WHERE appeal_remote_id = :id")
    suspend fun findByServerId(id: Long): Appeal?
}