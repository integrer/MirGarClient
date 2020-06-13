package org.mirgar.android.cryptoelection.operations

import kotlinx.coroutines.CoroutineScope
import org.mirgar.android.common.ui.OperationHandler
import org.mirgar.android.cryptoelection.failure.Failure

abstract class BaseOperationHandler : OperationHandler<OperationResult, Failure>() {

    override val exceptionClass = Failure::class

}

abstract class DelegatedOperationHandler(private val delegate: BaseOperationHandler) :
    BaseOperationHandler() {
    override fun beforeOperation() = delegate.beforeOperation()
    override fun afterOperation() = delegate.afterOperation()
    override fun handleResult(result: OperationResult) = delegate.handleResult(result)
    override fun handleException(exception: Failure) = delegate.handleException(exception)
    override fun handleUnknown(exception: Throwable) = delegate.handleUnknown(exception)
    override fun getScope(): CoroutineScope = delegate.getScope()
}
