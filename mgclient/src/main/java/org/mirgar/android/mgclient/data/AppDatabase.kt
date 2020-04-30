package org.mirgar.android.mgclient.data

import android.content.Context
import androidx.room.*
import org.mirgar.android.common.data.Converters as CommonConverters
import org.mirgar.android.mgclient.cfg.DB
import org.mirgar.android.mgclient.data.dao.*
import org.mirgar.android.mgclient.data.entity.*

@Database(
    entities = [Appeal::class, AppealPhoto::class, Category::class, RemoteAppealPhoto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CommonConverters::class, Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao

    abstract fun getAppealDao(): AppealDao

    abstract fun getAppealPhotoDao(): AppealPhotoDao

    abstract fun getRemoteAppealPhotoDao(): RemoteAppealPhotoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val monitor = Object()

        fun fromAppContext(context: Context): AppDatabase {
            return instance ?: synchronized(monitor) {
                instance ?: build(context).also { instance = it }
            }
        }

        private fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB.NAME).build()
        }
    }
}