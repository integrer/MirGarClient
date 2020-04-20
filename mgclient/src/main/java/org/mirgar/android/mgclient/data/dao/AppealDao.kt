package org.mirgar.android.mgclient.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.mirgar.android.mgclient.data.entity.Appeal
import org.mirgar.android.mgclient.data.models.AppealWithCategory
import org.mirgar.android.mgclient.data.models.AppealWithCategoryTitle

// TODO: Make all DAO interfaces internal
@Dao
interface AppealDao {
    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id WHERE is_own")
    fun getOwnWithCategoryTitle(): LiveData<List<AppealWithCategoryTitle>>

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    fun getById(id: Long): LiveData<Appeal>

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    suspend fun getByIdAsPlain(id: Long): Appeal?

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
}