package org.mirgar.android.mgclient.data

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.common.util.messaging.MessageDispatcher
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.dao.AppealDao
import org.mirgar.android.mgclient.data.entity.Appeal
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Integer.parseInt
import org.mirgar.android.mgclient.data.net.Repository as NetRepository
import org.mirgar.android.mgclient.data.net.models.AppealOut as NetAppeal
import org.mirgar.android.mgclient.data.net.models.AppealPhotoOut as NetPhoto

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

    @Throws(UserNotAuthenticatedException::class, ExceptionWithResources::class)
    suspend fun send(appeal: Appeal, messageChannel: MessageDispatcher) {
        val auth = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("auth", null)
            ?: throw UserNotAuthenticatedException()
        val transferableAppeal = toTransferable(appeal.id, messageChannel)
        val appealOnServer = netRepository.send(auth, transferableAppeal)
        dao.update(appeal.copy(serverId = appealOnServer.id.toLong()))
    }

    @Throws(ExceptionWithResources::class)
    suspend fun toTransferable(id: Long, messageChannel: MessageDispatcher) = coroutineScope {
        val appealFuture = async(Dispatchers.IO) { dao.getByIdAsPlain(id) }
        val photosFuture = async(Dispatchers.IO) {
            unitOfWork.appealPhotoRepository.getPhotosOfAppeal(id).map { photo ->
                photo.appealPhoto to async(Dispatchers.IO) {
                    try {
                        FileInputStream(photo.file).use { it.readBytes() }
                    } catch (_: FileNotFoundException) {
                        withContext(Dispatchers.Main) {
                            messageChannel.show({
                                this.getString(
                                    R.string.file_not_found,
                                    photo.appealPhoto.fileName
                                )
                            })
                        }
                        null
                    }
                }
            }.mapNotNull { (photo, photoFileContentFuture) ->
                photoFileContentFuture.await()?.let { NetPhoto.from(photo, it) }
            }
        }
        try {
            val appeal = appealFuture.await()
                ?: throw ExceptionWithResources { getString(R.string.appeal_not_found) }
            NetAppeal.from(appeal, photosFuture.await())
        } catch (ex: Exception) {
            photosFuture.cancel()
            throw ex
        }
    }
}