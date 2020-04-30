package org.mirgar.android.mgclient.ui.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.security.keystore.UserNotAuthenticatedException
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mirgar.android.common.exception.ExceptionWithResources
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.AppealStatus
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.entity.Appeal
import java.util.*

// ToDo: remove all works with [Context]
class AppealDetailsViewModel internal constructor(
    private val unitOfWork: UnitOfWork,
    private val context: Context
) : MutableViewModel<Long>() {

    /**
     * Two-way data binding property for [Appeal.title]
     */
    val title = MutableLiveData<String>()

    /**
     * Two-way data binding property for [Appeal.description]
     */
    val description = MutableLiveData<String>()

    val longitude = MutableLiveData<Double>()

    val latitude = MutableLiveData<Double>()

    private var appealOriginal = Appeal()
    private lateinit var locationManager: LocationManager

    private val _categoryName = MediatorLiveData<String?>().apply { value = null }
    val categoryName = _categoryName.map {
        if (categoryNameModifier != null) categoryNameModifier?.let { fn -> fn(it) }
        else it
    }

    val _canBeEdited = MediatorLiveData<Boolean>().apply {
        value = true
    }

    val canBeEdited: LiveData<Boolean> = _canBeEdited

    var categoryNameModifier: ((CharSequence?) -> CharSequence?)? = null

    private var locationListener: LocationListener? = null

    private val _isLocationFetching = MutableLiveData(false)
    val isLocationFetching: LiveData<Boolean> = _isLocationFetching

    lateinit var goToAuthorization: () -> Unit
    lateinit var goBack: () -> Unit


    private val appealMerger: MediatorLiveData<Appeal> = MediatorLiveData<Appeal>().apply {
        value = Appeal()
        addSource(title) { newVal ->
            value?.let { if (it.title != newVal) value = it.copy(title = newVal) }
        }
        addSource(description) { newVal ->
            value?.let { if (it.description != newVal) value = it.copy(description = newVal) }
        }
        addSource(longitude) { newVal ->
            value?.let { if (it.longitude != newVal) value = it.copy(longitude = newVal) }
        }
        addSource(latitude) { newVal ->
            value?.let { if (it.latitude != newVal) value = it.copy(latitude = newVal) }
        }
    }

    private fun initBindings(appeal: Appeal) {
        appealMerger.value = appeal
        title.value = appeal.title
        description.value = appeal.description
        longitude.value = appeal.longitude
        latitude.value = appeal.latitude
    }

    val imageListViewModel = ImageList(unitOfWork.appealPhotoRepository)

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            unitOfWork.appealRepository.delete(appealOriginal.id)
        }
    }

    //ToDo: Consider move all location work to separate class
    fun fetchLocation() {
        fun useLocation(location: Location) {
            latitude.value = location.latitude
            longitude.value = location.longitude
        }

        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _error.show(R.string.location_noPermission)
            return
        }

        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?.let {
                if (it.time > Calendar.getInstance().timeInMillis - 2 * 1000) {
                    useLocation(it)
                    return
                }
            }

        _isLocationFetching.value = true
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let { loc ->
                    useLocation(loc)
                    _isLocationFetching.value = false
                    if (locationListener == this) locationListener = null
                    locationManager.removeUpdates(this)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }.also {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, it)
        }

    }

    fun send() {
        if (isSaved.value == true) {
            viewModelScope.launch {
                try {
                    unitOfWork.appealRepository.send(appealOriginal, _message)
                    _message.show(R.string.appeal_saved)
                    goBack()
                } catch (_: UserNotAuthenticatedException) {
                    _message.show(R.string.authorization_required, R.string.authorize) {
                        goToAuthorization()
                    }
                } catch (ex: ExceptionWithResources) {
                    _error.show(ex)
                } catch (_: Exception) {
                    _error.show({ getString(R.string.unknown_error) })
                }
            }
        }
    }

    //region Standard hooks
    override suspend fun init(id: Long, lifecycleOwner: LifecycleOwner) {
        unitOfWork.appealRepository.let { repo ->
            if (!repo.hasAppeal(id)) {
                _error.show(R.string.appeal_not_found)
                return@let
            }
            val liveData = repo.getPreviewLiveById(id)
            liveData.observe(lifecycleOwner) {
                appealOriginal = it.appeal
            }
            _canBeEdited.addSource(liveData) {
                _canBeEdited.value = with(it.appeal) {
                    remoteId == null
                            || (userId == repo.userId
                            && when (status) {
                        AppealStatus.ACCEPTED, AppealStatus.PROCESSING, AppealStatus.DENIED -> false // TODO: Implement deleting appeal from server
                        else -> false
                    })
                }
            }
            _categoryName.addSource(liveData) { preview ->
                _categoryName.value = preview.categoryTitle
            }

            appealOriginal = repo.getOneAsPlain(id)!!
            initBindings(appealOriginal)

            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    override suspend fun save(id: Long) {
        unitOfWork.appealRepository.save(appealMerger.value!!)
    }

    override suspend fun create() = unitOfWork.appealRepository.new(appealMerger.value!!)
    //endregion

    //region Standard properties
    override val observable = appealMerger

    override val isValueSame
        get() = appealMerger.value == if (isSaved.value!!) appealOriginal else Appeal.default

    override fun onCleared() {
        locationListener?.let { locationManager.removeUpdates(it) }
    }
    //endregion

}
