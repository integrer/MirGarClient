package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import com.exonum.binding.common.crypto.CryptoFunction
import com.exonum.binding.common.crypto.KeyPair
import com.google.protobuf.MessageLite
import org.mirgar.android.cryptoelection.data.CryptoelectionTransaction
import org.mirgar.android.cryptoelection.data.ExonumKit
import org.mirgar.android.cryptoelection.data.ProtobufMessageFactory
import org.mirgar.android.cryptoelection.failure.CryptoelectionFailure
import org.mirgar.android.cryptoelection.model.CreateParticipant
import org.mirgar.android.cryptoelection.operations.OperationResult

abstract class CreateParticipantUseCase : TransactionUseCase() {
    lateinit var model: CreateParticipant
    abstract fun refreshKeys()
}

class CreateParticipantUseCaseImpl(
    override val exonumKit: ExonumKit,
    override val appContext: Context,
    private val cryptoFunction: CryptoFunction
) : CreateParticipantUseCase() {
    override val transactionType = CryptoelectionTransaction.CreateParticipant
    override val payload: MessageLite
        get() = ProtobufMessageFactory.createParticipant(model)

    override lateinit var keys: KeyPair private set

    override fun refreshKeys() {
        keys = cryptoFunction.generateKeyPair()
    }

    override val errorResolver = CryptoelectionFailure.Resolver

    private val attempts = 5

    override suspend fun run(): OperationResult.Registered {
        refreshKeys()
        for (i in 0 until attempts) {
            try {
                submitWithCheck()
                break
            } catch (f: CryptoelectionFailure.ParticipantAlreadyExists) {
                if (i < attempts - 1) refreshKeys() else throw f
            }
        }
        return OperationResult.Registered(keys.privateKey.toString())
    }
}
