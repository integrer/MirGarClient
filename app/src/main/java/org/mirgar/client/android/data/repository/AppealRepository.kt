package org.mirgar.client.android.data.repository

import org.mirgar.client.android.data.AppDatabase
import org.mirgar.client.android.data.dao.AppealDao

class AppealRepository private constructor(private val dao: AppealDao) {
    companion object {
        fun fromDao(dao: AppealDao) = AppealRepository(dao)

        fun fromDatabase(database: AppDatabase) = AppealRepository(database.getAppealDao())
    }
}