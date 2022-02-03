package org.mirgar.android.cryptoelection.operations

import org.mirgar.android.cryptoelection.model.PollGroupModel

sealed class OperationResult {

    data class Registered(val secretKey: String) : OperationResult()
    object SignedIn : OperationResult()
    object LoggedIn : OperationResult()
    object Voted : OperationResult()
    object Updated : OperationResult()
    object LoggedOut : OperationResult()

    data class ElectionResultsObtained(val value: Map<Int, Int>) : OperationResult()

    data class ElectionGroupsObtained(val value: List<PollGroupModel<String, Int>>) :
        OperationResult()
    // TODO: define operation results
}
