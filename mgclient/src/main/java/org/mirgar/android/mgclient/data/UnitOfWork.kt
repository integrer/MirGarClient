package org.mirgar.android.mgclient.data

import android.content.Context
import org.mirgar.android.mgclient.data.net.Repository as NetRepository

/**
 * Unit of work pattern implementation for [AppDatabase] class.
 *
 * @param context Interface to application information.
 */
class UnitOfWork(context: Context) {
    private val appContext: Context = context.applicationContext

    // Lazy for suppressing function reevaluation
    private val database: AppDatabase by lazy { AppDatabase.fromAppContext(appContext) }

    /**
     * Reference to [AppealRepository]
     */
    val appealRepository: AppealRepository get() = AppealRepository(database, appContext, this)

    /**
     * Reference to [CategoryRepository]
     */
    val categoryRepository: CategoryRepository get() = CategoryRepository(database)

    /**
     * Reference to [AppealPhotoRepository]
     */
    val appealPhotoRepository: AppealPhotoRepository get() =
        AppealPhotoRepository(database, appContext)
}