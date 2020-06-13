package org.mirgar.android.cryptoelection.viewmodel

import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun Kodein.Builder.bindViewModels() {
    bindViewModel<AuthorizationViewModel>() with provider {
        AuthorizationViewModelImpl(
            instance(),
            instance()
        )
    }
    bindViewModel<RegistrationViewModel>() with provider {
        RegistrationViewModel(
            instance(),
            instance()
        )
    }
    bindViewModel<ElectionsListViewModel>() with provider { ElectionsListViewModel() }
}
