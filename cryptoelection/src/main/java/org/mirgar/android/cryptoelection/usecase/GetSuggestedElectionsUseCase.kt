package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import org.mirgar.android.common.data.lazyMap
import org.mirgar.android.cryptoelection.data.SharedPreferencesManager
import org.mirgar.android.cryptoelection.model.PollGroupModel
import org.mirgar.android.cryptoelection.net.RestClient
import org.mirgar.android.cryptoelection.net.models.ElectionGroup
import org.mirgar.android.cryptoelection.utils.toPublicKey
import retrofit2.await

abstract class GetSuggestedElectionsUseCase : InternetUseCase() {
    abstract val result: List<PollGroupModel<String, Int>>
}

internal class GetSuggestedElectionsUseCaseImpl(
    override val appContext: Context, private val client: RestClient,
    private val sharedPrefsMgr: SharedPreferencesManager
) : GetSuggestedElectionsUseCase() {
    override lateinit var result: List<PollGroupModel<String, Int>> private set

    override suspend fun run() {
        val privateKey = sharedPrefsMgr.privateKey ?: throw IllegalStateException("No private key!")
        val hexPubKey = privateKey.toPublicKey().toString()
        result = client.getSuggestedElections(hexPubKey).await().lazyMap(ElectionGroup::toLocal)
    }
}
