package org.mirgar.client.android.data.dao

import androidx.lifecycle.LiveData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.mirgar.client.android.data.entity.Category
import org.mirgar.client.android.data.entity.CategoryWithSubcategories

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT * FROM categories WHERE category_id IN (SELECT DISTINCT(super_id) FROM categories)")
    fun getCategoriesWithSubcategories(): LiveData<List<CategoryWithSubcategories>>

    @Transaction
    @Query("SELECT * FROM categories WHERE category_id = :id")
    fun getSubcategories(id: Long): LiveData<CategoryWithSubcategories>

    @Query("SELECT * FROM categories WHERE super_id IS NULL")
    fun getRoots(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE super_id = :id LIMIT 1)")
    fun hasChild(id: Long): LiveData<Boolean>

    @Insert
    suspend fun insert(category: Category)
}