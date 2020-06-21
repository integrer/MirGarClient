package org.mirgar.android.cryptoelection.viewmodel

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun Kodein.Builder.bindViewModels() {
    bind<AuthorizationViewModel>() with provider {
        AuthorizationViewModelImpl(
            instance(),
            instance()
        )
    }
    bind<RegistrationViewModel>() with provider {
        RegistrationViewModel(
            instance(),
            instance()
        )
    }
    bind<ElectionsListViewModel>() with provider { ElectionsListViewModel() }
}
