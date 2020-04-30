package org.mirgar.android.mgclient.data

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.common.util.loadFileContent
import org.mirgar.android.common.util.messaging.MessageDispatcher
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.cfg.UPDATE_DELAY
import org.mirgar.android.mgclient.data.dao.AppealDao
import org.mirgar.android.mgclient.data.entity.Appeal
import org.mirgar.android.mgclient.data.models.AppealPreview
import java.util.*
import org.mirgar.android.mgclient.data.net.Repository as NetRepository
import org.mirgar.android.mgclient.data.net.models.AppealIn as NetAppealIn
import org.mirgar.android.mgclient.data.net.models.AppealOut as NetAppealOut
import org.mirgar.android.mgclient.data.net.models.AppealPhotoOut as NetPhoto

class AppealRepository internal constructor(
    private val db: AppDatabase,
    private val context: Context,
    private val unitOfWork: UnitOfWork
) {
    private val dao: AppealDao by lazy { db.getAppealDao() }

    private val netRepository by lazy { NetRepository() }

    private val photoRepository by lazy { unitOfWork.appealPhotoRepository }

    private val sharedPreferencesService by lazy { unitOfWork.sharedPreferencesService }

    fun getAppealsWithCategoryTitles(
        ownOnly: Boolean = true
    ): LiveData<List<AppealPreview>> {
        update()
        return if (ownOnly) userId?.let(dao::getOwnWithCategoryTitle)
            ?: dao.getOwnWithCategoryTitle()
        else dao.getAllWithCategoryTitle()
    }

    fun update(force: Boolean = false) {
        val necessaryToUpdate = sharedPreferencesService.io.appealsLastUpdated?.let {
            it.timeInMillis += UPDATE_DELAY
            it <= Calendar.getInstance()
        } ?: true

        if (necessaryToUpdate || force) GlobalScope.launch(Dispatchers.IO) {
            unitOfWork.categoryRepository.loadCategories()
            netRepository.getAllAppeals().forEach { updateFrom(it) }
            sharedPreferencesService.io.notifyAppealsUpdated()
        }
    }

    /**
     * Creates new appeal in local database
     */
    suspend fun new(): Long = new(Appeal(isOwn = true))

    suspend fun new(appeal: Appeal) = dao.insert(appeal)

    suspend fun save(appeal: Appeal) = dao.update(appeal)

    fun getOne(id: Long): LiveData<Appeal> = dao.getLiveById(id)

    fun getPreviewLiveById(id: Long) = dao.getPreviewLiveById(id)

    suspend fun getOneAsPlain(id: Long) = dao.getById(id)

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

        authorization.await().apply {
            sharedPreferencesService.authority.set(
                auth.let(::requireNotNull),
                id.let(::requireNotNull).let(Integer::parseInt)
            )
        }
    }

    val userId
        get() = context.getSharedPreferences("auth", Context.MODE_PRIVATE).run {
            if (contains("userId")) getInt("userId", 0).toLong()
            else null
        }

    val hasUserId
        get() = context.getSharedPreferences("auth", Context.MODE_PRIVATE).contains("userId")

    @Throws(UserNotAuthenticatedException::class, ExceptionWithResources::class)
    suspend fun send(appeal: Appeal, messageChannel: MessageDispatcher) {
        val auth = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("auth", null) ?: throw UserNotAuthenticatedException()
        val transferableAppeal = toTransferable(appeal.id, messageChannel)
        val appealOnServer = netRepository.send(auth, transferableAppeal)
        photoRepository.deleteAllBoundTo(appeal)
        photoRepository.update(appealOnServer.photos, appeal)
        updateFrom(appealOnServer, appeal)
    }

    @Throws(ExceptionWithResources::class)
    suspend fun toTransferable(id: Long, messageChannel: MessageDispatcher) = coroutineScope {
        val appeal = dao.getById(id)
        val photosFuture = async(Dispatchers.IO) {
            photoRepository.getPhotosOfAppeal(id).mapNotNull { photo ->
                try {
                    val photoFileContent = loadFileContent(photo.file)
                    NetPhoto.from(photo.appealPhoto, photoFileContent)
                } catch (ex: ExceptionWithResources) {
                    messageChannel.show(ex)
                    null
                }
            }
        }
        try {
            appeal ?: throw ExceptionWithResources { getString(R.string.appeal_not_found) }
            NetAppealOut.from(appeal, photosFuture.await())
        } catch (ex: Exception) {
            photosFuture.cancel()
            throw ex
        }
    }

    private suspend fun updateFrom(netAppeal: NetAppealIn, id: Long? = null) {
        id?.let { dao.getById(it) ?: return } ?: dao.findByServerId(netAppeal.id.toLong())
            ?.also { updateFrom_Impl(netAppeal, it) }
        ?: dao.insert(Appeal.default.apply(netAppeal))
    }

    private suspend fun updateFrom(netAppeal: NetAppealIn, appeal: Appeal) {
        if (hasAppeal(appeal.id)) updateFrom_Impl(netAppeal, appeal)
    }

    private suspend fun updateFrom_Impl(netAppeal: NetAppealIn, appeal: Appeal) {
        dao.update(appeal.apply(netAppeal))
    }
}
