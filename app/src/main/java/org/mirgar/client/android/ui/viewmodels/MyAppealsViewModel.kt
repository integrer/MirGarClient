package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.ViewModel

import org.mirgar.client.android.data.UnitOfWork

class MyAppealsViewModel(
    unitOfWork: UnitOfWork
) : ViewModel() {
    val appealsWithCategoryTitles = unitOfWork.appealRepository.ownAppealsWithCategoryTitles
}
