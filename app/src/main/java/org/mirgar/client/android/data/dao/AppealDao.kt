package org.mirgar.client.android.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Query
import org.mirgar.client.android.data.entity.Appeal

interface AppealDao {
    @Query("SELECT * FROM appeals")
    fun getAll(): LiveData<List<Appeal>>

    @Query("SELECT * FROM appeals WHERE is_own")
    fun getOwn(): LiveData<List<Appeal>>

    @Query("SELECT * FROM appeals WHERE NOT is_own")
    fun getAlien(): LiveData<List<Appeal>>
}