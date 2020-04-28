package org.mirgar.android.mgclient.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.common.ui.ActivityResult
import org.mirgar.android.common.util.Event
import org.mirgar.android.common.viewmodel.MessagingViewModel
import org.mirgar.android.mgclient.cfg.IMAGES_MAX
import org.mirgar.android.mgclient.data.AppealPhotoRepository
import org.mirgar.android.mgclient.ui.adapters.ImageAdapter
import java.io.File

class ImageList(private val repository: AppealPhotoRepository) : MessagingViewModel() {
    lateinit var deferredActionResultFactory: (Intent) -> Deferred<ActivityResult?>

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _isVisible

    private val _addEventChannel = MutableLiveData<Event<Unit>>()
    val addEventChannel: LiveData<Event<Unit>> = _addEventChannel

    private val _detailsEventChannel = MutableLiveData<Event<Long>>()
    val detailsEventChannel: LiveData<Event<Long>> = _detailsEventChannel

    private val addImageItem = AddImageItem(_addEventChannel)

    val adapter = ImageAdapter()

    fun init(id: Long, lifecycleOwner: LifecycleOwner) {
        _isVisible.value = true
        repository.getPhotosOfAppealAsLive(id)
            .observe(lifecycleOwner) { photos ->
                _isLoading.value = true
                // Preparing given sequence
                photos.asSequence().map { appealPhotoWithFile ->
                    RegularImageItem(
                        File(appealPhotoWithFile.fileName).toURI().toString(),
                        appealPhotoWithFile.id,
                        _detailsEventChannel
                    )
                }
                    .let { it + addImageItem }
                    .take(IMAGES_MAX)
                    .toList()
                    .let {
                        adapter.submitList(it) {
                            _isLoading.value = false
                        }
                    }
            }

        addEventChannel.observe(lifecycleOwner) { evt ->
            evt.unused?.let {
                viewModelScope.launch {
                    try {
                        withContext(Dispatchers.IO) {
                            repository.takePhoto(id, deferredActionResultFactory)
                        }
                    } catch (ex: ExceptionWithResources) {
                        _error.show(ex)
                    }
                }
            }
        }
    }
}
