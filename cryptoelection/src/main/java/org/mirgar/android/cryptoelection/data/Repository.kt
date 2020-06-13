package org.mirgar.android.cryptoelection.data

interface Repository {
    val isAuthorized: Boolean
}

internal class RepositoryImpl(
    private val sharedPreferencesManager: SharedPreferencesManager
) : Repository {

    override val isAuthorized get() = sharedPreferencesManager.hasPrivateKey
}
