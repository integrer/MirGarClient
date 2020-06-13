package org.mirgar.android.cryptoelection.data

import android.content.Context
import androidx.core.content.edit

interface SharedPreferencesManager {
    var privateKey: ByteArray?

    val hasPrivateKey: Boolean
}

class SharedPreferencesManagerImpl(private val context: Context) : SharedPreferencesManager {
    private val authPreferences by lazy {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    override var privateKey: ByteArray?
        get() = authPreferences.getString("privateKey", null)?.let(Converter::toBytes)
        set(value) {
            authPreferences.edit(commit = true) {
                value?.let { putString("privateKey", fromBytes(it)) } ?: remove("privateKey")
            }
        }

    override val hasPrivateKey: Boolean
        get() = authPreferences.getString("privateKey", null) != null

    private companion object Converter {
        fun fromBytes(bytes: ByteArray) = String(bytes, Charsets.ISO_8859_1)
        fun toBytes(byteString: String) = byteString.toByteArray(Charsets.ISO_8859_1)
    }
}
