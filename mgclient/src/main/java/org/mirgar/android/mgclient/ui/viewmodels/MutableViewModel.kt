package org.mirgar.android.mgclient.ui.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class MutableViewModel<ID> : ViewModel() {

    private val _id = MutableLiveData<ID?>(null)
    val id: LiveData<ID?> = _id

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _hasChanges = MutableLiveData(false)
    val hasChanges: LiveData<Boolean> = _hasChanges

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> = _saved

    private var isDataLoaded = false

    private lateinit var lifecycleOwner: LifecycleOwner

    @MainThread
    fun setup(id: ID?, owner: LifecycleOwner) {
        fun observe() {
            observable.observe(owner) {
                if (isSaved.value!!) {
                    // If model saved yet save it implicitly
                    save()
                } else {
                    // Otherwise just update [isChanged] state
                    if (isChanged != !isValueSame) isChanged = !isValueSame
                }
            }
        }

        if (!isDataLoaded && dataLoading.value != true) {
            _dataLoading.value = true

            if (id == null) {
                observe()
                lifecycleOwner = owner

                _dataLoading.value = false
                isDataLoaded = true
                return
            }

            _id.value = id
            viewModelScope.launch { init(id, owner) }
                .invokeOnCompletion { failure ->
                    failure?.also {
                        if (it !is CancellationException) {
                            TODO("Say to user what happen")
                        }
                    } ?: run {
                        // Job finished as well
                        observe()
                        isDataLoaded = true
                    }
                    _dataLoading.value = false
                }
        }
    }

    val isSaved = Transformations.map(_id) { it != null }

    private val mutex = Mutex()

    fun save() = viewModelScope.launch {
        mutex.withLock {
            if (!isValueSame) {
                _id.value?.let { save(it) }
                    ?: create().let {
                        isChanged = false
                        _id.value = it
                        init(it, lifecycleOwner)
                    }
            }
        }
    }

    private var isChanged
        get() = _hasChanges.value
        set(v) {
            _hasChanges.value = v
        }

    /**
     * Initializes view model
     */
    protected abstract suspend fun init(id: ID, lifecycleOwner: LifecycleOwner)

    /**
     * Saves view model
     */
    protected abstract suspend fun save(id: ID)

    /**
     * Creates new view model
     */
    protected abstract suspend fun create(): ID

    protected abstract val isValueSame: Boolean
    protected abstract val observable: LiveData<*>
}