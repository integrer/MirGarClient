package org.mirgar.android.mgclient.ui.viewmodels

import org.mirgar.android.common.viewmodel.MessagingViewModel

import org.mirgar.android.mgclient.data.UnitOfWork

class MyAppealsViewModel(
    unitOfWork: UnitOfWork
) : MessagingViewModel() {
    val appealsWithCategoryTitles = unitOfWork.appealRepository.ownAppealsWithCategoryTitles
}
