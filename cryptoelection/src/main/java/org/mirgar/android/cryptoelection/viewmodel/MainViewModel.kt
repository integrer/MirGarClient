package org.mirgar.android.cryptoelection.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.*
import org.mirgar.android.cryptoelection.data.Repository
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.usecase.LogoutUseCase

class MainViewModel(
    repository: Repository, private val logoutUseCase: LogoutUseCase,
    private val activityHandler: BaseOperationHandler
) : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isAuthorized = MutableLiveData<Boolean>(repository.isAuthorized)
    val isAuthorized: LiveData<Boolean> = _isAuthorized

    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var switchToAuthorization: () -> Unit
    lateinit var switchToNavigation: () -> Unit

    fun operationStarted() {
        _isLoading.postValue(true)
    }

    fun operationFinished() {
        _isLoading.postValue(false)
    }

    fun loggedIn() {
        _isAuthorized.postValue(true)
    }

    fun loggedOut() {
        _isAuthorized.postValue(false)
    }

    fun logOut() {
        activityHandler.withHandlerSync { logoutUseCase() }
    }

    @MainThread
    fun bindingsCompleted() {
        operationFinished()
        isAuthorized.distinctUntilChanged().observe(lifecycleOwner) { authorized ->
            if (authorized) switchToNavigation() else switchToAuthorization()
        }
    }
}