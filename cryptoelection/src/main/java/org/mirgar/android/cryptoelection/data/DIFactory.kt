package org.mirgar.android.cryptoelection.data

import android.content.Context

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

import org.mirgar.android.cryptoelection.usecase.bindUseCases
import org.mirgar.android.cryptoelection.viewmodel.bindViewModels

internal object DIFactory {
    fun getInstance(applicationContextFactory: () -> Context) = Kodein.lazy {
        bind<Context>(tag = "application") with singleton { applicationContextFactory() }

        bindDataFeatures()
        bindExonum()

        bindUseCases()
        bindViewModels()
    }

    private fun Kodein.Builder.bindDataFeatures() {
        bind<SharedPreferencesManager>() with provider {
            SharedPreferencesManagerImpl(instance(tag = "application"))
        }
        bind<Repository>() with provider { RepositoryImpl(instance()) }
    }
}
