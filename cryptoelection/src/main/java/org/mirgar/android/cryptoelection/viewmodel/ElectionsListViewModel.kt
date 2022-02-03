package org.mirgar.android.cryptoelection.viewmodel

import androidx.navigation.NavController
import org.mirgar.android.common.viewmodel.MessagingViewModel
import org.mirgar.android.cryptoelection.data.ElectionsStorage
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.ui.ElectionsListFragmentDirections
import org.mirgar.android.cryptoelection.usecase.GetSuggestedElectionsUseCase

class ElectionsListViewModel(
    private val useCase: GetSuggestedElectionsUseCase,
    private val operationHandler: BaseOperationHandler,
    val storage: ElectionsStorage
) : MessagingViewModel() {
    fun load() {
        operationHandler.withHandler { useCase() }
    }

    lateinit var navControllerFactory: () -> NavController

    fun open(address: String) {
        val dst = ElectionsListFragmentDirections.actionDstElectionsListToDstElection(address)
        navControllerFactory().navigate(dst)
    }
}