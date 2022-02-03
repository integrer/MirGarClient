package org.mirgar.android.cryptoelection.usecase

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun Kodein.Builder.bindUseCases() {
    bind<CreateParticipantUseCase>() with provider {
        CreateParticipantUseCaseImpl(instance(), instance(tag = "application"), instance())
    }
    bind<LoginUseCase>() with provider { LoginUseCaseImpl(instance()) }
    bind<LogoutUseCase>() with provider { LogoutUseCaseImpl(instance(), instance()) }
    bind<GetSuggestedElectionsUseCase>() with provider {
        GetSuggestedElectionsUseCaseImpl(instance(tag = "application"), instance(), instance())
    }
    bind<VoteUseCase>() with provider { VoteUseCaseImpl(instance(), instance(tag = "application")) }
    bind<GetElectionResultsUseCase>() with provider {
        GetElectionResultsUseCaseImpl(instance(), instance(tag = "application"))
    }
}
