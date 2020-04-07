package org.mirgar.client.android.data.repository

import androidx.lifecycle.LiveData
import org.mirgar.client.android.data.AppDatabase
import org.mirgar.client.android.data.dao.AppealDao
import org.mirgar.client.android.data.entity.Appeal

class AppealRepository private constructor(private val db: AppDatabase) {
    private val dao: AppealDao by lazy { db.getAppealDao() }

    val ownAppealsWithCategoryTitles
        get() = dao.getOwnWithCategoryTitle()

    val hasMyAppeals
        get() = dao.hasMyAppeals()

    /**
     * Creates new appeal in local database
     */
    suspend fun new(): Long = new(Appeal(0, isOwn = true))

    suspend fun new(appeal: Appeal) = dao.insert(appeal)

    suspend fun save(appeal: Appeal) = dao.update(appeal)

    fun getOne(id: Long): LiveData<Appeal> = dao.getById(id)

    suspend fun getOneAsPlain(id: Long) = dao.getByIdAsPlain(id)
    suspend fun delete(id: Long) {
        dao.delete(id)
    }

    companion object {
        fun fromDatabase(database: AppDatabase) = AppealRepository(database)
    }
}