package org.mirgar.android.mgclient.data

import android.content.Context
import android.content.ContextWrapper
import org.mirgar.android.mgclient.data.models.AppealPhotoWithFile
import java.io.File

class AppealPhotoRepository internal constructor(db: AppDatabase, context: Context) {
    private val dao = db.getAppealPhotoDao()

    private val photosHome = ContextWrapper(context).filesDir.path + "/photos"

    suspend fun getPhotosOfAppeal(id: Long) =
        dao.ofAppeal(id).map { appealPhoto ->
            AppealPhotoWithFile(appealPhoto, File("$photosHome/${appealPhoto.fileName}"))
        }
}