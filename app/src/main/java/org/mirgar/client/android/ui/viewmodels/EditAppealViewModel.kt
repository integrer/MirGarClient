package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import org.mirgar.client.android.data.UnitOfWork

import org.mirgar.client.android.data.entity.Appeal

class EditAppealViewModel(
    val appeal: LiveData<Appeal>
) : ViewModel() {
    class Factory(
        private val unitOfWork: UnitOfWork,
        private val appealId: Long
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditAppealViewModel(unitOfWork.appealRepository.getOne(appealId)) as T
        }
    }
}
