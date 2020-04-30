package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mirgar.android.mgclient.data.AppealStatus
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.AppealPreview

data class Appeal(
    private val inner: AppealPreview,
    private val unitOfWork: UnitOfWork
) : ViewModel() {
    val appealRepository = unitOfWork.appealRepository

    val appeal
        get() = inner.appeal

    val title
        get() = inner.appeal.title

    val categoryTitle
        get() = inner.categoryTitle

    val canBeDeleted
        get() = with(inner.appeal) {
            remoteId == null
                    || (userId == appealRepository.userId
                    && when (status) {
                AppealStatus.ACCEPTED, AppealStatus.DENIED -> false // TODO: Implement deleting appeal from server
                else -> false
            })
        }

    fun delete() {
        viewModelScope.launch {
            appealRepository.delete(appeal.id)
        }
    }
}
