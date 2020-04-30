package org.mirgar.android.mgclient.cfg

import org.mirgar.android.mgclient.BuildConfig

class Joomla private constructor(host: String, useSsl: Boolean) {
    val hostUrl = "${if (useSsl) "https" else "http"}://$host"

    companion object {
        private val debug by lazy { Joomla("10.0.2.2:8088", false) }
        private val release by lazy { Joomla("mirgar.ga", true) }
        val current get() = if (BuildConfig.DEBUG) debug else release
    }
}

object DB {
    const val NAME: String = "mirgar-db"
}

const val IMAGES_MAX = 5

const val UPDATE_DELAY = 60L * 60L * 1000L // Hour
