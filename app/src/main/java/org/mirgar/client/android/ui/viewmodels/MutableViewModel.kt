package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch

abstract class MutableViewModel<ID> : ViewModel() {

    private var id: ID? = null

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _hasChanges = MutableLiveData(false)
    val hasChanges: LiveData<Boolean> = _hasChanges

    private var isDataLoaded = false

    fun setup(optId: ID?, owner: LifecycleOwner) {
        if (!isDataLoaded && dataLoading.value != true) {
            _dataLoading.value = true
            optId?.let { id ->
                this.id = id
                viewModelScope.launch { init(id) }
                    .also { job ->
                        job.invokeOnCompletion {
                            it ?: setObserversFor(owner)
                            _dataLoading.value = false
                            isDataLoaded = true
                        }
                    }
            }
        }
    }

    protected abstract suspend fun init(id: ID)
    protected abstract fun setObserversFor(owner: LifecycleOwner)

    fun save() = viewModelScope.launch {
        _dataLoading.value = true
        try {
            id?.let { save(it) } ?: create().let { id = it }
            changed = false
        } finally {
            _dataLoading.value = false
        }
    }

    protected var changed
        get() = _hasChanges.value
        set(v) { _hasChanges.value = v }

    protected abstract suspend fun save(id: ID)
    protected abstract suspend fun create(): ID
}