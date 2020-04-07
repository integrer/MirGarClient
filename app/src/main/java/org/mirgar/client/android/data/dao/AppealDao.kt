package org.mirgar.client.android.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.mirgar.client.android.data.entity.Appeal
import org.mirgar.client.android.data.entity.AppealWithCategory
import org.mirgar.client.android.data.entity.AppealWithCategoryTitle

@Dao
interface AppealDao {
    @Query("SELECT * FROM appeals")
    fun getAll(): LiveData<List<Appeal>>

    @Query("SELECT * FROM appeals WHERE is_own")
    fun getOwn(): LiveData<List<Appeal>>

    @Query("SELECT * FROM appeals WHERE NOT is_own")
    fun getAlien(): LiveData<List<Appeal>>

    @Transaction
    @Query("SELECT * FROM appeals JOIN categories ON category_id = appeal_category_id")
    fun getAllWithCategory(): LiveData<List<AppealWithCategory>>

    @Transaction
    @Query("SELECT * FROM appeals JOIN categories ON category_id = appeal_category_id WHERE is_own")
    fun getOwnWithCategory(): LiveData<List<AppealWithCategory>>

    @Transaction
    @Query("SELECT * FROM appeals JOIN categories ON category_id = appeal_category_id WHERE NOT is_own")
    fun getAlienWithCategory(): LiveData<List<AppealWithCategory>>

    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id")
    fun getAllWithCategoryTitle(): LiveData<List<AppealWithCategoryTitle>>

    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id WHERE is_own")
    fun getOwnWithCategoryTitle(): LiveData<List<AppealWithCategoryTitle>>

    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id WHERE NOT is_own")
    fun getAlienWithCategoryTitle(): LiveData<List<AppealWithCategoryTitle>>

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    fun getById(id: Long): LiveData<Appeal>

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    suspend fun getByIdAsPlain(id: Long): Appeal?

    @Query("SELECT EXISTS(SELECT 1 FROM appeals WHERE is_own LIMIT 1)")
    fun hasMyAppeals(): LiveData<Boolean>

    @Insert
    suspend fun insert(appeal: Appeal): Long

    @Update
    suspend fun update(appeal: Appeal)

    @Query("DELETE FROM appeals WHERE appeal_id = :id")
    suspend fun delete(id: Long)

    @Delete
    suspend fun delete(appeal: Appeal)
}