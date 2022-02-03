package org.mirgar.android.cryptoelection.viewmodel

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun Kodein.Builder.bindViewModels() {
    bind<MainViewModel>() with provider { MainViewModel(instance(), instance(), instance()) }
    bind<AuthorizationViewModel>() with provider {
        AuthorizationViewModelImpl(instance(), instance())
    }
    bind<RegistrationViewModel>() with provider { RegistrationViewModel(instance(), instance()) }
    bind<ElectionsListViewModel>() with provider {
        ElectionsListViewModel(instance(), instance(), instance())
    }
    bind<ElectionViewModel>() with factory { address: String ->
        ElectionViewModel(address, instance(), instance(), instance(), instance(), instance())
    }
}
