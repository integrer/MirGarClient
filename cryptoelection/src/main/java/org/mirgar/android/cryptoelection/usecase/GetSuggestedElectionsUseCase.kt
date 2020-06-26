package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import org.mirgar.android.common.data.lazyMap
import org.mirgar.android.cryptoelection.model.PollGroupModel
import org.mirgar.android.cryptoelection.net.RestClient
import org.mirgar.android.cryptoelection.net.models.ElectionGroup
import retrofit2.await

abstract class GetSuggestedElectionsUseCase : InternetUseCase() {
    lateinit var address: String

    abstract val result: List<PollGroupModel<String, Int>>
}

internal class GetSuggestedElectionsUseCaseImpl(
    override val appContext: Context, private val client: RestClient
) : GetSuggestedElectionsUseCase() {
    override lateinit var result: List<PollGroupModel<String, Int>> private set

    override suspend fun run() {
        result = client.getSuggestedElections(address).await().lazyMap(ElectionGroup::toLocal)
    }
}
