package org.mirgar.android.mgclient.data

import android.content.Context
import androidx.core.content.edit
import java.util.*

class SharedPreferencesService(private val context: Context) {
    private val authSharedPreferences
        get() = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    private val ioSharedPreferences
        get() = context.getSharedPreferences("io", Context.MODE_PRIVATE)

    val authority = object : Authority {
        override fun set(auth: String, userId: Int) {
            authSharedPreferences.edit(commit = true) {
                putString("auth", auth)
                putInt("userId", userId)
            }
        }

        override fun unset() {
            authSharedPreferences.edit(commit = true) {
                remove("auth")
                remove("userId")
            }
        }

        override val hasUserId get() = authSharedPreferences.contains("userId")

        override val userId
            get() = authSharedPreferences.takeIf { it.contains("userId") }
                ?.getInt("userId", 0)

        override val auth
            get() = authSharedPreferences.takeIf { it.contains("auth") }
                ?.getString("auth", null)
    }

    interface Authority {
        fun set(auth: String, userId: Int)
        fun unset()
        val hasUserId: Boolean
        val userId: Int?
        val auth: String?
    }

    val io = object : IO {
        override var appealsLastUpdated: Calendar?
            get() = getStoredCalendar("appealsLastUpdated")
            private set(value) = storeCalendar("appealsLastUpdated", value)

        override fun notifyAppealsUpdated() {
            appealsLastUpdated = Calendar.getInstance()
        }

        override var categoriesLastUpdated: Calendar?
            get() = getStoredCalendar("categoriesLastUpdated")
            private set(value) = storeCalendar("categoriesLastUpdated", value)

        override fun notifyCategoriesUpdated() {
            appealsLastUpdated = Calendar.getInstance()
        }

        private fun getStoredCalendar(name: String) = ioSharedPreferences
            .takeIf { it.contains(name) }
            ?.let {
                it.getLong(name, 0L)
                    .let { Calendar.getInstance().apply { timeInMillis = it } }
            }

        private fun storeCalendar(name: String, value: Calendar?) {
            ioSharedPreferences.edit(commit = true) {
                value?.let {
                    putLong(name, it.timeInMillis)
                } ?: remove(name)
            }
        }
    }

    interface IO {
        val appealsLastUpdated: Calendar?
        fun notifyAppealsUpdated()

        val categoriesLastUpdated: Calendar?
        fun notifyCategoriesUpdated()
    }
}