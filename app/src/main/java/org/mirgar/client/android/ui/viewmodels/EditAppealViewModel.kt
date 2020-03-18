package org.mirgar.client.android.ui.viewmodels

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner

import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.data.entity.Appeal

class EditAppealViewModel private constructor(
    val appeal: LiveData<Appeal>
) : ViewModel() {
    class Factory(
        private val unitOfWork: UnitOfWork,
        private val appealId: Long,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

        override fun <T : ViewModel?> create(
            _key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            @Suppress("UNCHECKED_CAST")
            return EditAppealViewModel(unitOfWork.appealRepository.getOne(appealId)) as T
        }
    }
}
