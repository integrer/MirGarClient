package org.mirgar.android.cryptoelection.operations

sealed class OperationResult {

    class Registered(val secretKey: String) : OperationResult()
    class SignedIn : OperationResult()
    // TODO: define operation results
}
