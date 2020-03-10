package org.mirgar.client.android.data

import android.content.Context

import org.mirgar.client.android.data.repository.AppealRepository
import org.mirgar.client.android.data.repository.CategoryRepository

/**
 * Unit of work pattern implementation for [AppDatabase] class.
 *
 * @param context Interface to application information.
 */
class UnitOfWork (context: Context) {
    private val appContext: Context = context.applicationContext

    // Lazy for suppressing function reevaluation
    private val database: AppDatabase by lazy { AppDatabase.fromAppContext(appContext) }

    /**
     * Reference to [AppealRepository]. Created lazy for more performance.
     */
    val appealRepository: AppealRepository by lazy { AppealRepository.fromDatabase(database) }

    /**
     * Reference to [CategoryRepository]. Created lazy for more performance.
     */
    val categoryRepository: CategoryRepository by lazy { CategoryRepository.fromDatabase(database) }
}