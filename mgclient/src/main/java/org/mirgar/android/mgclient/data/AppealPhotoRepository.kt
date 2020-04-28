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
import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.common.ui.ActivityResult
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.entity.AppealPhoto
import org.mirgar.android.mgclient.data.models.AppealPhotoWithFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AppealPhotoRepository internal constructor(db: AppDatabase, private val context: Context) {
    private val dao = db.getAppealPhotoDao()

    private val photosHome = ContextWrapper(context).filesDir.path + "/photos"

    suspend fun getPhotosOfAppeal(id: Long) =
        dao.ofAppeal(id).map { appealPhoto ->
            AppealPhotoWithFile(appealPhoto, File(appealPhoto.fileName))
        }

    fun getPhotosOfAppealAsLive(id: Long) = dao.ofAppealAsLive(id)

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
            if (file.length() == 0L) throw ExceptionWithResources { getString(R.string.unknown_error) }
            savePhoto(appealId, file.absolutePath, "jpeg")
        } catch (ex: ExceptionWithResources) {
            if (file.exists()) file.delete()
            throw ex
        } catch (ex: IOException) {
            if (file.exists()) file.delete()
            throw ExceptionWithResources(cause = ex) { getString(R.string.unknown_error) }
        }
    }

    private suspend fun savePhoto(appealId: Long, absolutePath: String, extension: String) {
        dao.insert(AppealPhoto(appealId = appealId, fileName = absolutePath, ext = extension))
    }

    fun getLivePhoto(id: Long) = dao.getOneAsLive(id).map { appealPhoto ->
        appealPhoto?.let { AppealPhotoWithFile(it, File(it.fileName)) }
    }

    suspend fun delete(id: Long) {
        val photo = dao.getOne(id) ?: return
        if (photo.fileName.startsWith(photosHome)) {
            val file = File(photo.fileName)
            if (file.exists()) file.delete()
        }
        dao.delete(photo)
    }
}