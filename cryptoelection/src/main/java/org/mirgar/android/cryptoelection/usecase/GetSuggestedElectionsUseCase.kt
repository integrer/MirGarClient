package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import com.exonum.binding.common.crypto.PublicKey
import org.mirgar.android.common.data.lazyMap
import org.mirgar.android.cryptoelection.data.SharedPreferencesManager
import org.mirgar.android.cryptoelection.model.PollGroupModel
import org.mirgar.android.cryptoelection.net.RestClient
import org.mirgar.android.cryptoelection.net.models.ElectionGroup
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
        val byteKeys = (sharedPrefsMgr.privateKey ?: throw IllegalStateException())
            .drop(32)
            .toByteArray()
        val pubKey = PublicKey.fromBytes(byteKeys).toString()
        result = client.getSuggestedElections(pubKey).await().lazyMap(ElectionGroup::toLocal)
    }
}
