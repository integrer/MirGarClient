package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import com.exonum.binding.common.crypto.CryptoFunction
import com.exonum.binding.common.crypto.CryptoFunctions
import com.exonum.binding.common.crypto.KeyPair
import com.exonum.client.ExonumClient
import com.google.protobuf.MessageLite
import org.mirgar.android.cryptoelection.data.*
import org.mirgar.android.cryptoelection.data.ProtobufMessageFactory
import org.mirgar.android.cryptoelection.failure.CryptoelectionFailure
import org.mirgar.android.cryptoelection.failure.GeneralExonumErrorResolver
import org.mirgar.android.cryptoelection.failure.TransactionFailure
import org.mirgar.android.cryptoelection.model.CreateParticipant
import kotlin.time.times

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

    override suspend fun run() {
        refreshKeys()
        for (i in 0 until attempts) {
            try {
                super.run()
                return
            } catch (f: CryptoelectionFailure.ParticipantAlreadyExists) {
                if (i < attempts - 1) refreshKeys() else throw f
            }
        }
    }
}
