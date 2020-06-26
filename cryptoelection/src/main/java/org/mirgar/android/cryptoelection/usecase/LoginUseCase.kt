package org.mirgar.android.cryptoelection.usecase

import com.exonum.binding.common.crypto.PrivateKey

import org.mirgar.android.cryptoelection.data.SharedPreferencesManager
import org.mirgar.android.cryptoelection.failure.CommonFailure
import java.util.*

abstract class LoginUseCase : UseCase {
    lateinit var secretKey: String

    companion object {
        const val SECRET_KEY_LENGTH = 64
    }
}

internal class LoginUseCaseImpl(private val sharedPreferencesManager: SharedPreferencesManager) :
    LoginUseCase() {
    private fun validateKey() {
        val regex = Regex("[0-9a-f]*", RegexOption.IGNORE_CASE)
        if (!regex.matches(secretKey) || secretKey.length != SECRET_KEY_LENGTH)
            throw CommonFailure.MalformedKey()
    }

    override fun invoke() {
        validateKey()
        // TODO: Check for participant existence
        sharedPreferencesManager.privateKey =
            PrivateKey.fromHexString(secretKey.toLowerCase(Locale.ROOT))
    }
}
