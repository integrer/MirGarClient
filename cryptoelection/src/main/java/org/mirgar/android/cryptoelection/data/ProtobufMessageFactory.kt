package org.mirgar.android.cryptoelection.data

import com.exonum.messages.crypto.Types
import com.google.protobuf.ByteString
import org.mirgar.android.cryptoelection.messages.Service
import org.mirgar.android.cryptoelection.messages.Wrappers
import org.mirgar.android.cryptoelection.model.CreateParticipant

internal object ProtobufMessageFactory {
    fun createParticipant(
        name: String,
        email: String,
        passCode: String,
        phoneNumber: String,
        residence: ByteString?
    ): Service.CreateParticipant = Service.CreateParticipant.newBuilder()
        .setName(name)
        .setEmail(email)
        .setPassCode(passCode)
        .setPhoneNumber(phoneNumber)
        .setResidence(optionalHash(residence))
        .build()

    fun createParticipant(model: CreateParticipant) = with(model) {
        createParticipant(name, email, passCode, phoneNumber, residence)
    }

    fun optionalHash(value: ByteString?): Wrappers.OptionalHash =
        Wrappers.OptionalHash.newBuilder().apply {
            value?.let { setValue(hash(it)) }
        }.build()

    fun hash(data: ByteString): Types.Hash = Types.Hash.newBuilder().setData(data).build()
}