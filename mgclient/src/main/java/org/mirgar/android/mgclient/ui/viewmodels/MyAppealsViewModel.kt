package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.ViewModel

import org.mirgar.android.mgclient.data.UnitOfWork

class MyAppealsViewModel(
    unitOfWork: UnitOfWork
) : ViewModel() {
    val appealsWithCategoryTitles = unitOfWork.appealRepository.ownAppealsWithCategoryTitles
}
