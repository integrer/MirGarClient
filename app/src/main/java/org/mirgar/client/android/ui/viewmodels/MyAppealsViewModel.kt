package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.data.entity.AppealWithCategoryTitle

class MyAppealsViewModel(
    val appealsWithCategoryTitles: List<AppealWithCategoryTitle>
) : ViewModel() {
    class Factory(private val unitOfWork: UnitOfWork) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MyAppealsViewModel(unitOfWork.appealRepository.myAppealsWithCategoryTitles) as T
        }
    }
}
