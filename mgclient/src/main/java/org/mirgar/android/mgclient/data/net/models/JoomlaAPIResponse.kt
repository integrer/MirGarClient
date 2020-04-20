package org.mirgar.android.mgclient.data.net.models

import kotlin.properties.Delegates

class JoomlaAPIResponse<D : Any> {
    var err_msg: String? = null
    var err_code: String? = null
    lateinit var response_id: String
    lateinit var api: String
    lateinit var version: String
    lateinit var data: D
}