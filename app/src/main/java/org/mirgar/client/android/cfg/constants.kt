package org.mirgar.client.android.cfg

class Joomla(val host: String, val useSsl: Boolean, val apiPluginName: String) {
    companion object {
        private const val API_PLUGIN_NAME: String = "mirgar"

        fun debug() = Joomla("", false, API_PLUGIN_NAME)
        fun release() = Joomla("mirgar.ga", true, API_PLUGIN_NAME)
    }
}

class DB {
    companion object {
        const val NAME: String = "mirgar-db"
    }
}
