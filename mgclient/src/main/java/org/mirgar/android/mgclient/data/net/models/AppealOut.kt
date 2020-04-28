package org.mirgar.android.mgclient.data.net.models

import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.mgclient.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates
import org.mirgar.android.mgclient.data.entity.Appeal as DBAppeal
import org.mirgar.android.mgclient.data.entity.AppealPhoto as DBAppealPhoto

class AppealOut {
    lateinit var name: String
    var description: String? = null
    var cat_id by Delegates.notNull<Int>()
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var photos: List<AppealPhotoOut>

    companion object {
        fun from(appeal: DBAppeal, photoOuts: List<AppealPhotoOut> = listOf()) = AppealOut().apply {
            name = appeal.title
            description = appeal.description
            cat_id = appeal.categoryId?.toInt()
                ?: throw ExceptionWithResources { getString(R.string.no_category) }
            latitude = appeal.latitude
            longitude = appeal.longitude
            this.photos = photoOuts
        }

        fun from(appeal: DBAppeal, photos: Sequence<AppealPhotoOut>) = from(appeal, photos.toList())
    }
}

class AppealPhotoOut {
    lateinit var name: String
    lateinit var ext: String
    lateinit var content: ByteArray

    companion object {
        private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)

        fun from(photo: DBAppealPhoto, fileContent: ByteArray) = AppealPhotoOut().apply {
            ext = photo.ext
            name = photo.run { caption ?: dateFormat.format(created.time) }
            content = fileContent
        }
    }
}
