package org.mirgar.client.android.data.repository

import androidx.lifecycle.LiveData

import org.mirgar.client.android.data.AppDatabase
import org.mirgar.client.android.data.dao.AppealDao
import org.mirgar.client.android.data.entity.Appeal
import org.mirgar.client.android.data.entity.AppealWithCategoryTitle

class AppealRepository private constructor(private val db: AppDatabase) {
    private val dao: AppealDao by lazy { db.getAppealDao() }

    val myAppealsWithCategoryTitles: List<AppealWithCategoryTitle>
        get() = dao.getOwnWithCategoryTitle()

    val hasMyAppeals: Boolean
        get() = dao.hasMyAppeals()

    /**
     * Creates new appeal in local database
     */
    fun new(): Long = dao.insert(Appeal(0, isOwn = true))

    fun getOne(id: Long): LiveData<Appeal> = dao.getById(id)

    companion object {
        fun fromDatabase(database: AppDatabase) = AppealRepository(database)
    }
}