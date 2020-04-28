package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mirgar.android.common.viewmodel.MessagingViewModel
import org.mirgar.android.mgclient.data.UnitOfWork
import kotlin.properties.Delegates

class ViewAppealPhoto(unitOfWork: UnitOfWork) : MessagingViewModel() {

    private val repository = unitOfWork.appealPhotoRepository

    lateinit var uri: LiveData<String?> private set
    lateinit var goBack: () -> Unit
    var id by Delegates.notNull<Long>()

    fun init(id: Long) {
        this.id = id
        uri = repository.getLivePhoto(id).map {
            it?.file?.toURI().toString()
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.delete(id)
            goBack()
        }
    }
}