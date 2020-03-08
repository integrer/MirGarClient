package org.mirgar.client.android.data

import androidx.lifecycle.LiveData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id IN (SELECT DISTINCT(super_id) FROM categories)")
    fun getCategoriesWithSubcategories(): LiveData<List<CategoryRelations>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :id")
    fun getSubcategories(id: Long): LiveData<CategoryRelations>

    @Query("SELECT * FROM categories WHERE super_id IS NULL")
    fun getRoots(): LiveData<List<Category>>

    @Insert
    fun insert(category: Category)
}