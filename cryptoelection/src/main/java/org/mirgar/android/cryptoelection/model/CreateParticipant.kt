package org.mirgar.android.cryptoelection.model

import com.google.protobuf.ByteString

data class CreateParticipant(
    val name: String = "",
    val email: String = "",
    val passCode: String = "",
    val phoneNumber: String = "",
    val residence: ByteString? = null
)
