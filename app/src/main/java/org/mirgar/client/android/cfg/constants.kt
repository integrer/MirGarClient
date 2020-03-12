package org.mirgar.client.android.cfg

class Joomla private constructor(val host: String, val useSsl: Boolean, val apiPluginName: String) {
    companion object {
        private const val API_PLUGIN_NAME: String = "mirgar"

        val debug by lazy { Joomla("127.0.0.1:8080", false, API_PLUGIN_NAME) }
        val release by lazy { Joomla("mirgar.ga", true, API_PLUGIN_NAME) }
    }
}

class DB {
    companion object {
        const val NAME: String = "mirgar-db"
    }
}
