package org.mirgar.client.android.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CategoryDao {
    fun getAll(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT * FROM categories WHERE id IN (SELECT DISTINCT(super_id) FROM categories)")
    fun getCategoriesWithSubcategories(): LiveData<List<CategoryRelations>>
}