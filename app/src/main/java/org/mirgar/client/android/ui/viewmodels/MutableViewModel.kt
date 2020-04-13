package org.mirgar.client.android.ui.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class MutableViewModel<ID> : ViewModel() {

    private var id: ID? = null

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _hasChanges = MutableLiveData(false)
    val hasChanges: LiveData<Boolean> = _hasChanges

    private var isDataLoaded = false

    private lateinit var lifecycleOwner: LifecycleOwner

    @MainThread
    fun setup(id: ID?, owner: LifecycleOwner) {
        fun observe() {
            observable.observe(owner) {
                if (isSaved) {
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

            this.id = id
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

    protected val isSaved get() = id != null

    private val mutex = Mutex()

    fun save() = viewModelScope.launch {
        mutex.withLock {
            if (!isValueSame) {
                id?.let { save(it) }
                    ?: create().let {
                        isChanged = false
                        id = it
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