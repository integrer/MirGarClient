package org.mirgar.client.android.data.repository

import org.mirgar.client.android.data.AppDatabase
import org.mirgar.client.android.data.dao.CategoryDao

class CategoryRepository private constructor(private val dao: CategoryDao) {
    companion object {
        /**
         * Creates self object from dao object
         * @param dao Dao instance
         */
        fun fromDao(dao: CategoryDao): CategoryRepository = CategoryRepository(dao)
        /**
         * Creates self object from database object
         * @param database Database instance
         */
        fun fromDatabase(database: AppDatabase): CategoryRepository =
            fromDao(database.getCategoryDao())
    }
}