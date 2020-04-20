package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mirgar.android.mgclient.data.AppealRepository

class Authorization(private val appealRepository: AppealRepository) : ViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    lateinit var onAfterLogin: Runnable

    fun login() {
        _isLoading.value = true
        //TODO: Add form validation
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