package org.mirgar.android.mgclient.cfg

import android.content.Context

class Joomla private constructor(val host: String, useSsl: Boolean, val apiPluginName: String) {
    val hostUrl = "${if (useSsl) "https" else "http"}://$host"

    companion object {
        private const val API_PLUGIN_NAME: String = "mirgar"

        val debug by lazy { Joomla("10.0.2.2:8088", false, API_PLUGIN_NAME) }
        val release by lazy { Joomla("mirgar.ga", true, API_PLUGIN_NAME) }
        val current get() =
            if (org.mirgar.android.mgclient.BuildConfig.DEBUG)
                debug
            else
                release
    }
}

class DB {
    companion object {
        const val NAME: String = "mirgar-db"
    }
}
