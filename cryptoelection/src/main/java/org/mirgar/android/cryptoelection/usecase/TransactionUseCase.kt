package org.mirgar.android.cryptoelection.usecase

import com.exonum.binding.common.crypto.KeyPair
import com.exonum.binding.common.hash.HashCode
import com.exonum.binding.common.message.TransactionMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.mirgar.android.cryptoelection.data.CryptoelectionTransaction
import org.mirgar.android.cryptoelection.data.ExonumKit
import org.mirgar.android.cryptoelection.failure.GeneralExonumErrorResolver
import org.mirgar.android.cryptoelection.failure.TransactionFailure

abstract class TransactionUseCase : InternetUseCase() {
    protected abstract val exonumKit: ExonumKit
    protected abstract val transactionType: CryptoelectionTransaction

    private val serviceInfo
        get() = exonumKit.servicesConfiguration.cryptoelectionServiceInfo.require()

    protected abstract val payload: com.google.protobuf.MessageLite
    protected abstract val keys: KeyPair

    protected open val errorResolver: GeneralExonumErrorResolver =
        GeneralExonumErrorResolver.Default

    private val transaction: TransactionMessage
        get() = TransactionMessage.builder()
            .serviceId(serviceInfo.id)
            .transactionId(transactionType.id)
            .payload(payload)
            .sign(keys)

    private suspend fun submitTransaction(message: TransactionMessage): HashCode = coroutineScope {
        withContext(Dispatchers.IO) {
            // This method is working in blocking way
            exonumKit.client.submitTransaction(message)
        }
    }

    private suspend fun checkTransactionFinished(hash: HashCode) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                val configuration = exonumKit.gettingTransactionConfiguration
                val retryDelay = configuration.delay.toMillis()
                for (i in 0 until configuration.attempts) {
                    delay(retryDelay)

                    val transactionInformation = exonumKit.client.getTransaction(hash)
                        .orElseThrow { TransactionFailure.NotFound() }

                    if (transactionInformation.isCommitted) {
                        val result = transactionInformation.executionResult
                        if (!result.hasError()) return@withContext
                        else throw TransactionFailure.FailedToCommitRaw(result.error)
                    }
                }
            }
        }
    }

    @Suppress("WEAKER_ACCESS")
    var lastTransactionHash: HashCode? = null
        protected set

    suspend fun submitWithCheck() {
        try {
            val hash = submitTransaction(transaction)
            checkTransactionFinished(hash)
            lastTransactionHash = hash
        } catch (ex: TransactionFailure.FailedToCommitRaw) {
            throw errorResolver.resolve(ex)
        }
    }
}