package org.mirgar.android.mgclient.data.net.models

import java.util.Date
import kotlin.properties.Delegates

class Appeal {
    var id: Int? = null
    lateinit var name: String
    var description: String? = null
    var cat_id by Delegates.notNull<Int>()
    var type_id: Int? = null
    var user_id: Int? = null
    var answer: String? = null
    var date_answer: Date? = null
    var date_start: Date? = null
    var region_id: Int? = null
    var address: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var photos: List<AppealPhoto>
}

class AppealPhoto {
    lateinit var name: String
    lateinit var ext: String
    lateinit var content: ByteArray
}
