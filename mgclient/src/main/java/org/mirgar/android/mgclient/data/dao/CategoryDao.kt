package org.mirgar.android.mgclient.data.dao

import androidx.lifecycle.LiveData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.mirgar.android.mgclient.data.entity.Category
import org.mirgar.android.mgclient.data.models.CategoryWithStatus
import org.mirgar.android.mgclient.data.models.CategoryWithSubcategories

@Dao
interface CategoryDao {
    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM categories LIMIT 1)")
    suspend fun hasAny(): Boolean

    @Transaction
    @Query(
        """
    WITH appeal(category_id) AS (SELECT appeal_category_id AS category_id FROM appeals
        WHERE appeal_id = :appealId LIMIT 1)
    SELECT
        categories.*,
        appeal.category_id = categories.category_id as is_related,
        EXISTS(SELECT 1 FROM categories nested WHERE nested.super_id = categories.category_id
                LIMIT 1) as has_subcategories,
        :appealId as appeal_id
    FROM categories JOIN appeal
    WHERE categories.super_id = :superId
    ORDER BY category_title
        """
    )
    fun getChildrenWithStatus(appealId: Long, superId: Long): LiveData<List<CategoryWithStatus>>

    @Transaction
    @Query(
        """
    WITH appeal(category_id) AS (SELECT appeal_category_id AS category_id FROM appeals
        WHERE appeal_id = :appealId LIMIT 1)
    SELECT
        categories.*,
        appeal.category_id = categories.category_id as is_related,
        EXISTS(SELECT 1 FROM categories nested WHERE nested.super_id = categories.category_id
            LIMIT 1) as has_subcategories,
        :appealId as appeal_id
    FROM categories JOIN appeal
    WHERE categories.super_id IS NULL
    ORDER BY category_title
        """
    )
    fun getChildrenWithStatus(appealId: Long): LiveData<List<CategoryWithStatus>>

    @Insert
    suspend fun insertAll(categories: Iterable<Category>)

    @Query("SELECT super_id FROM categories WHERE category_id = :id LIMIT 1")
    suspend fun getSuperId(id: Long): Long?
}