package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import org.mirgar.android.cryptoelection.net.RestClient
import org.mirgar.android.cryptoelection.operations.OperationResult
import retrofit2.await

abstract class GetElectionResultsUseCase : InternetUseCase() {
    lateinit var key: String
}

internal class GetElectionResultsUseCaseImpl(
    private val client: RestClient, override val appContext: Context
) : GetElectionResultsUseCase() {
    override suspend fun run(): OperationResult.ElectionResultsObtained {
        val result = client.getElectionResults(key).await()
        return OperationResult.ElectionResultsObtained(result)
    }
}
