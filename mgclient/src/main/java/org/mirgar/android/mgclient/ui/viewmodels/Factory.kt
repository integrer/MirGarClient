package org.mirgar.android.mgclient.ui.viewmodels

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mirgar.android.mgclient.data.UnitOfWork

@Suppress("UNCHECKED_CAST")
class Factory(private val context: Context) : ViewModelProvider.Factory {
    private val unitOfWork = UnitOfWork(context)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.run {
            when {
                isAssignableFrom(EditAppeal::class.java) -> EditAppeal(unitOfWork, context)
                isAssignableFrom(MyAppealsViewModel::class.java) -> MyAppealsViewModel(unitOfWork)
                isAssignableFrom(SelectCategory::class.java) -> SelectCategory(unitOfWork)
                isAssignableFrom(Authorization::class.java) -> Authorization(unitOfWork.appealRepository)
                else -> throw UnsupportedOperationException("Unable to produce $name")
            }
        } as T
}

val Fragment.viewModelFactory get() = Factory(requireContext())
