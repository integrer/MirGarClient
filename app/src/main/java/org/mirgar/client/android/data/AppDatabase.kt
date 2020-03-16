package org.mirgar.client.android.data

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import org.mirgar.client.android.cfg.DB
import org.mirgar.client.android.data.dao.AppealDao
import org.mirgar.client.android.data.dao.CategoryDao
import org.mirgar.client.android.data.entity.Appeal
import org.mirgar.client.android.data.entity.Category

@Database(entities = [Appeal::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao

    abstract fun getAppealDao(): AppealDao

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