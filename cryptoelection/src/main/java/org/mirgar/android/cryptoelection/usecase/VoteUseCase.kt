package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import com.exonum.binding.common.crypto.KeyPair
import com.exonum.messages.crypto.Types
import com.google.protobuf.ByteString
import com.google.protobuf.MessageLite
import com.goterl.lazycode.lazysodium.LazySodiumAndroid
import com.goterl.lazycode.lazysodium.SodiumAndroid
import com.goterl.lazycode.lazysodium.interfaces.Random
import org.mirgar.android.cryptoelection.data.CryptoelectionTransaction
import org.mirgar.android.cryptoelection.data.ExonumKit
import org.mirgar.android.cryptoelection.messages.Service
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.utils.toBytes
import java.math.BigInteger
import kotlin.properties.Delegates

abstract class VoteUseCase : TransactionUseCase() {
    lateinit var electionId: String

    var optionId by Delegates.notNull<Int>()

    public override lateinit var keys: KeyPair
}

internal class VoteUseCaseImpl(
    override val exonumKit: ExonumKit,
    override val appContext: Context
) : VoteUseCase() {
    override val transactionType = CryptoelectionTransaction.Vote

    override val payload: MessageLite
        get() = Service.Vote.newBuilder()
            .setElectionId(decodeToHash(electionId))
            .setOptionId(optionId)
            .setSeed(generateSeed())
            .build()


    override suspend fun run(): OperationResult {
        submitWithCheck()
        return OperationResult.Voted
    }

    companion object {
        private fun generateSeed(): Long {
            val bytes = (LazySodiumAndroid(SodiumAndroid()) as Random).nonce(8).reversedArray()
            return BigInteger(bytes).toLong()
        }

        private fun decodeToHash(hexString: String): Types.Hash = Types.Hash.newBuilder()
            .setData(ByteString.copyFrom(hexString.toBytes()))
            .build()
    }
}
