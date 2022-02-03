package org.mirgar.android.cryptoelection.usecase

import org.mirgar.android.cryptoelection.data.ElectionsStorage
import org.mirgar.android.cryptoelection.data.SharedPreferencesManager
import org.mirgar.android.cryptoelection.operations.OperationResult

abstract class LogoutUseCase : UseCase

internal class LogoutUseCaseImpl(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val electionsStorage: ElectionsStorage
) : LogoutUseCase() {
    override fun invoke(): OperationResult.LoggedOut {
        if (!sharedPreferencesManager.hasPrivateKey) throw IllegalStateException()
        sharedPreferencesManager.privateKey = null
        electionsStorage.clear()
        return OperationResult.LoggedOut
    }
}
