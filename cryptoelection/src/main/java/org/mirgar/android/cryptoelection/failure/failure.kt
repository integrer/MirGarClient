package org.mirgar.android.cryptoelection.failure

import com.exonum.messages.core.runtime.Errors

sealed class Failure : RuntimeException()

sealed class CommonFailure : Failure() {
    class NoServiceInformation : CommonFailure()
    class NoInternetConnection : CommonFailure()
    class MalformedKey : CommonFailure()
    class UnableToConnect(val destination: String?) : CommonFailure()
    class Unknown : CommonFailure()
}

sealed class PermissionFailure : Failure() {
    class LackAccessNetworkState : PermissionFailure()
    class LackAccessBackgroundLocation : PermissionFailure()
    class LackInternetPermission : PermissionFailure()
}

sealed class TransactionFailure : Failure() {
    class NotFound : TransactionFailure()
    class FailedToCommitRaw(val rawError: Errors.ExecutionError) : TransactionFailure()
    open class GeneralCommitFailure(override val cause: Throwable? = null) : TransactionFailure()
}

abstract class GeneralExonumErrorResolver {
    open fun resolve(ex: TransactionFailure.FailedToCommitRaw): TransactionFailure.GeneralCommitFailure {
        // TODO: Resolve raw error
        return TransactionFailure.GeneralCommitFailure(ex)
    }

    object Default : GeneralExonumErrorResolver()
}
