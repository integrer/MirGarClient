package org.mirgar.android.mgclient.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mirgar.android.mgclient.data.UnitOfWork

@Suppress("UNCHECKED_CAST") // TODO: Consider delete context from factory
class Factory(private val context: Context, private val unitOfWork: UnitOfWork) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.run {
            when {
                isAssignableFrom(AppealDetailsViewModel::class.java) -> AppealDetailsViewModel(unitOfWork, context)
                isAssignableFrom(MyAppealsViewModel::class.java) -> MyAppealsViewModel(unitOfWork)
                isAssignableFrom(SelectCategory::class.java) -> SelectCategory(unitOfWork)
                isAssignableFrom(Authorization::class.java) -> Authorization(
                    unitOfWork.appealRepository,
                    context
                )
                isAssignableFrom(ViewAppealPhoto::class.java) -> ViewAppealPhoto(unitOfWork)
                else -> throw UnsupportedOperationException("Unable to produce $name")
            }
        } as T
}
