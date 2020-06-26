package org.mirgar.android.cryptoelection.data

import android.content.Context
import androidx.core.content.edit
import com.exonum.binding.common.crypto.PrivateKey

interface SharedPreferencesManager {
    var privateKey: PrivateKey?

    val hasPrivateKey: Boolean
}

class SharedPreferencesManagerImpl(private val context: Context) : SharedPreferencesManager {
    private val authPreferences by lazy {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    override var privateKey: PrivateKey?
        get() = authPreferences.getString("privateKey", null)?.let(Converter::toPublicKey)
        set(value) = authPreferences.edit(commit = true) {
            value?.let { putString("privateKey", from(it)) } ?: remove("privateKey")
        }

    override val hasPrivateKey: Boolean
        get() = authPreferences.getString("privateKey", null) != null

    private companion object Converter {
        private val charset = Charsets.ISO_8859_1
        fun from(value: PrivateKey) = fromBytes(value.toBytes())
        fun toPublicKey(byteString: String) = PrivateKey.fromBytes(toBytes(byteString))
        fun fromBytes(bytes: ByteArray) = String(bytes, charset)
        fun toBytes(byteString: String) = byteString.toByteArray(charset)
    }
}
