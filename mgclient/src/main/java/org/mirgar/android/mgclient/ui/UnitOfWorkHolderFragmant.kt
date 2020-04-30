package org.mirgar.android.mgclient.ui

import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import org.mirgar.android.common.ui.MessagingFragment
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.ui.viewmodels.Factory

abstract class UnitOfWorkHolderFragment : MessagingFragment {
    constructor() : super()

    @ContentView
    @Suppress("Unused")
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected val unitOfWork by lazy { UnitOfWork(requireContext()) }

    protected val viewModelFactory by lazy { Factory(requireContext(), unitOfWork) }
}