package org.mirgar.android.mgclient.data.net.models


class JoomlaAPIResponse<D : Any> {
    var err_msg: String? = null
    var err_code: String? = null
    lateinit var response_id: String
    lateinit var api: String
    lateinit var version: String
    var data: D? = null
}