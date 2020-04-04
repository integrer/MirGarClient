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
    fun getAllWithCategory(): List<AppealWithCategory>

    @Transaction
    @Query("SELECT * FROM appeals JOIN categories ON category_id = appeal_category_id WHERE is_own")
    fun getOwnWithCategory(): List<AppealWithCategory>

    @Transaction
    @Query("SELECT * FROM appeals JOIN categories ON category_id = appeal_category_id WHERE NOT is_own")
    fun getAlienWithCategory(): List<AppealWithCategory>

    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id")
    fun getAllWithCategoryTitle(): List<AppealWithCategoryTitle>

    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id WHERE is_own")
    fun getOwnWithCategoryTitle(): List<AppealWithCategoryTitle>

    @Transaction
    @Query("SELECT appeals.*, category_title FROM appeals LEFT JOIN categories ON category_id = appeal_category_id WHERE NOT is_own")
    fun getAlienWithCategoryTitle(): List<AppealWithCategoryTitle>

    @Query("SELECT * FROM appeals WHERE appeal_id = :id LIMIT 1")
    fun getById(id: Long): LiveData<Appeal>

    @Query("SELECT EXISTS(SELECT 1 FROM appeals WHERE is_own LIMIT 1)")
    fun hasMyAppeals(): Boolean

    @Insert
    fun insert(appeal: Appeal): Long

    @Update
    fun update(appeal: Appeal)

    @Delete
    fun delete(appeal: Appeal)
}