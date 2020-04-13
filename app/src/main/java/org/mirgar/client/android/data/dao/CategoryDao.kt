package org.mirgar.client.android.data.dao

import androidx.lifecycle.LiveData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.mirgar.client.android.data.entity.Category
import org.mirgar.client.android.data.entity.CategoryWithStatus
import org.mirgar.client.android.data.entity.CategoryWithSubcategories

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<Category>>

    @Transaction
    @Query("""
        SELECT * FROM categories WHERE category_id IN (SELECT DISTINCT(super_id) FROM categories)
        """)
    fun getCategoriesWithSubcategories(): LiveData<List<CategoryWithSubcategories>>

    @Transaction
    @Query("SELECT * FROM categories WHERE category_id = :id")
    fun getSubcategories(id: Long): LiveData<CategoryWithSubcategories>

    @Query("SELECT * FROM categories WHERE super_id IS NULL")
    fun getRoots(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE super_id = :id LIMIT 1)")
    fun hasChild(id: Long): Boolean

    @Transaction
    @Query(
        """
    SELECT
        categories.*,
        categories.category_id = (SELECT appeal_category_id AS category_id FROM appeals
            WHERE appeal_id = :appealId LIMIT 1) as status,
        :appealId as appeal_id
    FROM categories
    WHERE categories.super_id = :superId
        """
    )
    fun getChildrenWithStatus(superId: Long?, appealId: Long): LiveData<List<CategoryWithStatus>>

    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT super_id FROM categories WHERE category_id = :id LIMIT 1")
    suspend fun getSuperId(id: Long): Long?
}