package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.AppealWithCategoryTitle

data class Appeal(
    private val inner: AppealWithCategoryTitle,
    private val unitOfWork: UnitOfWork
) : ViewModel() {
    val appeal
        get() = inner.appeal

    val title
        get() = inner.appeal.title

    val categoryTitle
        get() = inner.categoryTitle

    fun delete() {
        viewModelScope.launch {
            unitOfWork.appealRepository.delete(appeal.id)
        }
    }
}