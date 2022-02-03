package org.mirgar.android.cryptoelection.utils

import com.exonum.binding.common.crypto.PrivateKey
import com.exonum.binding.common.crypto.PublicKey
import com.google.common.io.BaseEncoding

fun PrivateKey.toPublicKey(): PublicKey =
    PublicKey.fromBytes(toBytes().let { it.sliceArray(IntRange(32, it.size - 1)) })

fun String.toBytes() = BaseEncoding.base16().lowerCase().decode(this)
