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
     * Reference to [AppealRepository]
     */
    val appealRepository: AppealRepository get() = AppealRepository.fromDatabase(database)

    /**
     * Reference to [CategoryRepository]
     */
    val categoryRepository: CategoryRepository get() = CategoryRepository.fromDatabase(database)
}