package org.mirgar.android.cryptoelection.viewmodel

import androidx.lifecycle.ViewModel
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.usecase.LoginUseCase

abstract class AuthorizationViewModel : ViewModel() {
    abstract fun login(secretKey: String)
}

internal class AuthorizationViewModelImpl(private val handler: BaseOperationHandler, private val loginUseCase: LoginUseCase) : AuthorizationViewModel() {
    override fun login(secretKey: String) {
        loginUseCase.secretKey = secretKey
        handler.withHandlerSync { loginUseCase() }
    }
}