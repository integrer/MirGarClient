package org.mirgar.android.cryptoelection.operations

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mirgar.android.common.ui.OperationHandler
import org.mirgar.android.cryptoelection.R
import org.mirgar.android.cryptoelection.failure.Failure

abstract class BaseOperationHandler : OperationHandler<OperationResult, Failure>() {

    override val exceptionClass = Failure::class

    abstract val context: Context

    protected fun alert(
        @StringRes msg: Int = R.string.error_unknown, @StringRes title: Int = R.string.error
    ) = alert(context.getString(msg), title)

    protected fun alert(msg: CharSequence, @StringRes title: Int = R.string.error) {
        getScope().launch {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }
}

abstract class DelegatedOperationHandler(private val delegate: BaseOperationHandler) :
    BaseOperationHandler() {
    override fun beforeOperation() = delegate.beforeOperation()
    override fun afterOperation() = delegate.afterOperation()
    override fun handleResult(result: OperationResult) = delegate.handleResult(result)
    override fun handleException(exception: Failure) = delegate.handleException(exception)
    override fun handleUnknown(exception: Throwable) = delegate.handleUnknown(exception)
    override fun getScope(): CoroutineScope = delegate.getScope()
    override val context: Context
        get() = delegate.context
}
