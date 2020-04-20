package org.mirgar.android.mgclient.data

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.mirgar.android.mgclient.data.dao.AppealDao
import org.mirgar.android.mgclient.data.entity.Appeal
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Integer.parseInt
import org.mirgar.android.mgclient.data.net.Repository as NetRepository
import org.mirgar.android.mgclient.data.net.models.Appeal as NetAppeal
import org.mirgar.android.mgclient.data.net.models.AppealPhoto as NetPhoto

class AppealRepository internal constructor(
    private val db: AppDatabase,
    private val context: Context,
    private val unitOfWork: UnitOfWork
) {
    private val dao: AppealDao by lazy { db.getAppealDao() }

    private val netRepository by lazy { NetRepository() }

    val ownAppealsWithCategoryTitles
        get() = dao.getOwnWithCategoryTitle()

    /**
     * Creates new appeal in local database
     */
    suspend fun new(): Long = new(Appeal(isOwn = true))

    suspend fun new(appeal: Appeal) = dao.insert(appeal)

    suspend fun save(appeal: Appeal) = dao.update(appeal)

    fun getOne(id: Long): LiveData<Appeal> = dao.getById(id)

    suspend fun getOneAsPlain(id: Long) = dao.getByIdAsPlain(id)

    suspend fun setCategory(appealId: Long, categoryId: Long? = null) {
        dao.setCategory(appealId, categoryId)
    }

    suspend fun delete(id: Long) {
        dao.delete(id)
    }

    suspend fun hasAppeal(id: Long) = dao.has(id)

    suspend fun categoryIdOf(id: Long): Long? = dao.getCategoryId(id)

    suspend fun authorize(username: String, password: String) = coroutineScope {
        val authorization = async(Dispatchers.IO) { netRepository.authorize(username, password) }

        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        authorization.await().apply {
            editor.putString("auth", auth)
            editor.putInt("userId", requireNotNull(id?.let { parseInt(it) }))
            editor.apply()
        }
    }

    @Throws(UserNotAuthenticatedException::class)
    suspend fun send(appeal: Appeal) = coroutineScope {
        val auth = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("auth", null)
            ?: throw UserNotAuthenticatedException()
        val sendingDeferred = async(Dispatchers.IO) {
            netRepository.send(auth, toTransferable(appeal.id))
        }
        launch(Dispatchers.IO) {
            dao.update(appeal.copy(serverId = sendingDeferred.await().toLong()))
        }
    }

    suspend fun toTransferable(id: Long) = coroutineScope {
        val appealFuture = async(Dispatchers.IO) { dao.getByIdAsPlain(id) }
        val photosFuture = async(Dispatchers.IO) {
            unitOfWork.appealPhotoRepository.getPhotosOfAppeal(id).map { photo ->
                photo to async(Dispatchers.IO) {
                    try {
                        FileInputStream(photo.file).use { it.readBytes() }
                    } catch (_: FileNotFoundException) {
                        null
                    }
                }
            }.mapNotNull { (photo, photoFileContentFuture) ->
                photoFileContentFuture.await()?.let { byteArray ->
                    NetPhoto().apply {
                        ext = photo.appealPhoto.ext
                        name = photo.appealPhoto.run { caption ?: created.toString() }
                        content = byteArray
                    }
                }
            }
        }
        appealFuture.await()?.let { appeal ->
            NetAppeal().apply {
                this.id = appeal.serverId?.toInt()
                name = appeal.title
                description = appeal.description
                cat_id = appeal.categoryId?.toInt()
                    ?: throw NullPointerException("Category is required")
                latitude = appeal.latitude
                longitude = appeal.longitude
                photos = photosFuture.await()
            }
        } ?: run {
            photosFuture.cancel()
            throw NullPointerException("Appeal not found")
        }
    }
}