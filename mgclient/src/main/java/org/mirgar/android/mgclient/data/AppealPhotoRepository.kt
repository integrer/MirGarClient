package org.mirgar.android.mgclient.data

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.map
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mirgar.android.common.R as CommonR
import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.common.ui.ActivityResult
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.entity.Appeal
import org.mirgar.android.mgclient.data.entity.AppealPhoto
import org.mirgar.android.mgclient.data.entity.RemoteAppealPhoto
import org.mirgar.android.mgclient.data.models.AppealPhotoWithFile
import org.mirgar.android.mgclient.data.net.models.AppealPhotoIn
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AppealPhotoRepository internal constructor(db: AppDatabase, private val context: Context) {
    private val daoRemotes = db.getRemoteAppealPhotoDao()
    private val daoLocals = db.getAppealPhotoDao()

    private val photosHome = ContextWrapper(context).filesDir.path + "/photos"

    suspend fun getPhotosOfAppeal(id: Long) =
        daoLocals.ofAppeal(id).map { appealPhoto ->
            AppealPhotoWithFile(appealPhoto, File(appealPhoto.fileName))
        }

    fun getPhotosOfAppealAsLive(id: Long) = daoLocals.ofAppealAsLive(id)

    @Throws(IOException::class)
    suspend fun createFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val dir = File(photosHome)
        if (!dir.exists()) dir.mkdir() || throw AssertionError("Unable to create images dir")
        return withContext(Dispatchers.IO) { File.createTempFile(timestamp, ".jpg", dir) }
    }

    @Throws(ExceptionWithResources::class)
    suspend fun takePhoto(
        appealId: Long,
        deferredActivityResultFactory: (Intent) -> Deferred<ActivityResult?>
    ) {
        lateinit var file: File
        try {
            file = withContext(Dispatchers.IO) { createFile() }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                val uri = FileProvider.getUriForFile(context, "org.mirgar.android.mgclient", file)
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            // TODO: use lifecycle observer instead
            val result = deferredActivityResultFactory(intent).await()
            result ?: throw ExceptionWithResources { getString(R.string.camera_app_not_found) }
            when (result.resultCode) {
                android.app.Activity.RESULT_OK -> {
                    if (file.length() == 0L)
                        throw ExceptionWithResources { getString(R.string.unknown_error) }
                    savePhoto(appealId, file.absolutePath, "jpeg")
                }
                android.app.Activity.RESULT_CANCELED ->
                    throw ExceptionWithResources { getString(CommonR.string.canceled) }
                else -> throw ExceptionWithResources { getString(R.string.unknown_error) }
            }
        } catch (ex: ExceptionWithResources) {
            if (file.exists()) file.delete()
            throw ex
        } catch (ex: IOException) {
            if (file.exists()) file.delete()
            throw ExceptionWithResources(cause = ex) { getString(R.string.unknown_error) }
        }
    }

    private suspend fun savePhoto(appealId: Long, absolutePath: String, extension: String) {
        daoLocals.insert(AppealPhoto(appealId = appealId, fileName = absolutePath, ext = extension))
    }

    fun getLivePhoto(id: Long) = daoLocals.getOneAsLive(id).map { appealPhoto ->
        appealPhoto?.let { AppealPhotoWithFile(it, File(it.fileName)) }
    }

    suspend fun delete(id: Long) {
        delete_impl(daoLocals.getOne(id) ?: return)
    }

    private suspend fun delete_impl(photo: AppealPhoto) {
        if (photo.fileName.startsWith(photosHome)) {
            val file = File(photo.fileName)
            if (file.exists()) file.delete()
        }
        daoLocals.delete(photo)
    }

    suspend fun deleteAllBoundTo(appeal: Appeal) {
        daoLocals.ofAppeal(appeal.id).forEach { delete_impl(it) }
    }

    suspend fun update(photos: List<AppealPhotoIn>, owner: Appeal) {
        photos.asSequence().map { RemoteAppealPhoto(it.id.toLong(), owner.id, it.related_path) }
            .forEach { daoRemotes.updateOrInsert(it) }
    }
}