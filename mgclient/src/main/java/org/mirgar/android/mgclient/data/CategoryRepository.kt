package org.mirgar.android.mgclient.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mirgar.android.mgclient.data.dao.CategoryDao
import org.mirgar.android.mgclient.data.models.CategoryWithStatus
import org.mirgar.android.mgclient.data.net.Repository as NetRepository

class CategoryRepository internal constructor(private val db: AppDatabase) {
    private val dao: CategoryDao by lazy { db.getCategoryDao() }

    private val netRepository by lazy { NetRepository() }

    suspend fun childrenWithStatus(
        categoryId: Long?,
        appealId: Long
    ): LiveData<List<CategoryWithStatus>> {
        return coroutineScope {
            var loadingCategoriesJob: Deferred<Unit>? = null
            if (!dao.hasAny()) {
                loadingCategoriesJob =
                    async(Dispatchers.IO) { dao.insertAll(netRepository.getCategories()) }
            }

            val result = categoryId?.let { dao.getChildrenWithStatus(appealId, it) }
                ?: dao.getChildrenWithStatus(appealId)
            loadingCategoriesJob?.await()
            result
        }
    }

    suspend fun superIdOf(id: Long): Long? = dao.getSuperId(id)
}