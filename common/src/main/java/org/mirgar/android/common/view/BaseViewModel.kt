package org.mirgar.android.common.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    protected val _error = MessageDispatcher()
    val error: LiveData<SnackbarSuite> = _error.event

    protected val _message = MessageDispatcher()
    val message: LiveData<SnackbarSuite> = _message.event
}
