package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch

abstract class MutableViewModel<ID> : ViewModel() {

    private var id: ID? = null

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private var isDataLoaded = false

    fun setup(id: ID?, owner: LifecycleOwner) {
        if (!isDataLoaded && dataLoading.value != true) {
            _dataLoading.value = true
            try {
                id?.let {
                    this.id = it
                    viewModelScope.launch { init(it) }
                }

                setObserversFor(owner)
            } finally {
                isDataLoaded = true
            }
        }
    }

    protected abstract suspend fun init(id: ID)
    protected abstract fun setObserversFor(owner: LifecycleOwner)

    fun save() = viewModelScope.launch {
        _dataLoading.value = true
        try {
            id?.let { save(it) } ?: create().let { id = it }
        } finally {
            _dataLoading.value = false
        }
    }

    protected abstract suspend fun save(id: ID)
    protected abstract suspend fun create(): ID
}