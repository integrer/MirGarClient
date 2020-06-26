package org.mirgar.android.cryptoelection.utils

import com.exonum.binding.common.crypto.PrivateKey
import com.exonum.binding.common.crypto.PublicKey

fun PrivateKey.toPublicKey(): PublicKey =
    PublicKey.fromBytes(toBytes().sliceArray(IntRange(32, Int.MAX_VALUE)))
