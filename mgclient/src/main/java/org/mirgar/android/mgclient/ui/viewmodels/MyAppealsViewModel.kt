package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.LiveData
import org.mirgar.android.common.viewmodel.MessagingViewModel

import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.AppealPreview

class MyAppealsViewModel(unitOfWork: UnitOfWork) : MessagingViewModel() {
    private val appealRepository = unitOfWork.appealRepository
    private val sharedPreferencesService = unitOfWork.sharedPreferencesService

    val isAuthorized: Boolean get() = sharedPreferencesService.authority.hasUserId

    lateinit var appealsWithCategoryTitles: LiveData<List<AppealPreview>>

    fun init(ownOnly: Boolean) {
        appealsWithCategoryTitles = appealRepository.getAppealsWithCategoryTitles(ownOnly)
    }
}
