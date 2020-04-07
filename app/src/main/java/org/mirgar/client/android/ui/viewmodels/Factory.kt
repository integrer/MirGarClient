package org.mirgar.client.android.ui.viewmodels

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mirgar.client.android.data.UnitOfWork

@Suppress("UNCHECKED_CAST")
class Factory(context: Context) : ViewModelProvider.Factory {
    private val unitOfWork = UnitOfWork(context)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.run {
            when {
                isAssignableFrom(EditAppeal::class.java) -> EditAppeal(unitOfWork)
                isAssignableFrom(MyAppealsViewModel::class.java) -> MyAppealsViewModel(unitOfWork)
                else -> throw UnsupportedOperationException("Unable to produce $name")
            }
        } as T
}

val Fragment.viewModelFactory get() = Factory(requireContext())
