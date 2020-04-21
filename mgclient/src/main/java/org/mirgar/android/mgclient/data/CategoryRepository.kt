package org.mirgar.android.mgclient.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.mirgar.android.mgclient.data.dao.CategoryDao
import org.mirgar.android.mgclient.data.net.Repository as NetRepository

class CategoryRepository internal constructor(private val db: AppDatabase) {
    private val dao: CategoryDao by lazy { db.getCategoryDao() }

    private val netRepository by lazy { NetRepository() }

    fun childrenWithStatus(
        categoryId: Long?,
        appealId: Long
    ) = categoryId?.let { dao.getChildrenWithStatus(appealId, it) }
        ?: dao.getChildrenWithStatus(appealId)

    suspend fun loadCategories() = coroutineScope {
        if (!dao.hasAny()) {
            launch(Dispatchers.IO) { dao.insertAll(netRepository.getCategories()) }
        }
    }

    suspend fun superIdOf(id: Long): Long? = dao.getSuperId(id)
}