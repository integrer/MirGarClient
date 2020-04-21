package org.mirgar.android.mgclient.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.mirgar.android.common.view.BaseViewModel
import org.mirgar.android.mgclient.data.AppealRepository
import org.mirgar.android.mgclient.R

class Authorization(
    private val appealRepository: AppealRepository,
    private val context: Context
) : BaseViewModel() {
    val username = MutableLiveData("")
    val usernameError: LiveData<String?> = username.map { v ->
        if (v.isNullOrBlank())
            context.getString(R.string.username_required)
        else null
    }

    val password = MutableLiveData("")
    val passwordError: LiveData<String?> = password.map { v ->
        if (v.isNullOrEmpty())
            context.getString(R.string.password_required)
        else null
    }

    private val hasError
        get() = usernameError.value?.let { _ -> true }
        ?: passwordError.value?.let { _ -> true }
        ?: false

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    lateinit var onAfterLogin: Runnable

    fun login() {
        if (hasError) return
        _isLoading.value = true
        viewModelScope.launch {
            appealRepository.authorize(username.value!!, password.value!!)
        }.invokeOnCompletion { failure ->
            failure ?: run {
                onAfterLogin.run()
            }
            _isLoading.value = false
        }
    }
}