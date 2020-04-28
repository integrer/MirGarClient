package org.mirgar.android.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.mirgar.android.common.util.messaging.MessageDispatcher
import org.mirgar.android.common.util.messaging.SnackbarSuite

abstract class MessagingViewModel : ViewModel() {
    protected val _error = MessageDispatcher()
    val errorChannel: LiveData<SnackbarSuite> = _error.event

    protected val _message = MessageDispatcher()
    val messageChannel: LiveData<SnackbarSuite> = _message.event
}
