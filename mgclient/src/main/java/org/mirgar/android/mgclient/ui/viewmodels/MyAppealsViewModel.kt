package org.mirgar.android.mgclient.ui.viewmodels

import org.mirgar.android.common.view.BaseViewModel

import org.mirgar.android.mgclient.data.UnitOfWork

class MyAppealsViewModel(
    unitOfWork: UnitOfWork
) : BaseViewModel() {
    val appealsWithCategoryTitles = unitOfWork.appealRepository.ownAppealsWithCategoryTitles
}
