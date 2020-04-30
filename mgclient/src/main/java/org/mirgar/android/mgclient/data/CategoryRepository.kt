package org.mirgar.android.mgclient.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.mirgar.android.mgclient.cfg.UPDATE_DELAY
import org.mirgar.android.mgclient.data.dao.CategoryDao
import java.util.*
import org.mirgar.android.mgclient.data.net.Repository as NetRepository

class CategoryRepository internal constructor(private val db: AppDatabase, unitOfWork: UnitOfWork) {
    private val dao: CategoryDao by lazy { db.getCategoryDao() }

    private val netRepository by lazy { NetRepository() }

    private val sharedPreferencesService by lazy { unitOfWork.sharedPreferencesService }

    fun childrenWithStatus(
        categoryId: Long?,
        appealId: Long
    ) = categoryId?.let { dao.getChildrenWithStatus(appealId, it) }
        ?: dao.getChildrenWithStatus(appealId)

    suspend fun loadCategories() = coroutineScope {
        val necessaryToUpdate = sharedPreferencesService.io.categoriesLastUpdated?.let {
            it.timeInMillis += UPDATE_DELAY
            it <= Calendar.getInstance()
        } ?: true // TODO: Updating Mechanism

        if (!dao.hasAny()) {
            launch(Dispatchers.IO) {
                dao.insertAll(netRepository.getCategories())
                sharedPreferencesService.io.notifyCategoriesUpdated()
            }
        }
    }

    suspend fun superIdOf(id: Long): Long? = dao.getSuperId(id)
}