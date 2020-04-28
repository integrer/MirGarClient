package org.mirgar.android.mgclient.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mirgar.android.common.viewmodel.MessagingViewModel
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.AppealRepository

class Authorization(
    private val appealRepository: AppealRepository,
    private val context: Context
) : MessagingViewModel() {
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