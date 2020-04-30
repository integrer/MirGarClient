package org.mirgar.android.mgclient.data.net.models

import java.util.*
import kotlin.properties.Delegates

class AppealIn {
    var id: Int by Delegates.notNull()
    lateinit var name: String
    var description: String? = null
    var cat_id: Int by Delegates.notNull()
    var type_id: Int? = null
    var user_id: Int? = null
    var answer: String? = null
    var date_answer: Date? = null
    var date_start: Date? = null
    var region_id: Int? = null
    var address: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var photos: List<AppealPhotoIn> = listOf()
}

class AppealPhotoIn {
    var id: Int by Delegates.notNull()
    lateinit var related_path: String
}
