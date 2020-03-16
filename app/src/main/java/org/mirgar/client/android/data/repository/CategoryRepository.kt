package org.mirgar.client.android.data.repository

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import org.mirgar.client.android.data.AppDatabase
import org.mirgar.client.android.data.dao.CategoryDao
import org.mirgar.client.android.data.entity.Appeal

class CategoryRepository private constructor(private val db: AppDatabase) {
    companion object {
        /**
         * Creates self object from database object
         * @param database Database instance
         */
        fun fromDatabase(database: AppDatabase): CategoryRepository =
            CategoryRepository(database)
    }

    private val dao: CategoryDao by lazy { db.getCategoryDao() }
}