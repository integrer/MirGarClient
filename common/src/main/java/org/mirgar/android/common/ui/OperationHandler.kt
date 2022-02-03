package org.mirgar.android.common.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class OperationHandler<TResult, TException: Throwable> {
    abstract fun beforeOperation()

    abstract fun afterOperation()

    abstract fun handleResult(result: TResult)

    abstract fun handleException(exception: TException)

    abstract fun handleUnknown(exception: Throwable)

    abstract val exceptionClass: KClass<TException>

    abstract fun getScope(): CoroutineScope

    inline fun withHandler(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        crossinline operation: suspend CoroutineScope.() -> TResult
    ) {
        getScope().launch(context, start) {
            try {
                beforeOperation()
                val result = operation()
                handleResult(result)
            } catch (ex: Throwable) {
                if (exceptionClass.isInstance(ex))
                    @Suppress("UNCHECKED_CAST")
                    handleException(ex as TException)
                else handleUnknown(ex)
            } finally {
                afterOperation()
            }
        }
    }

    inline fun withHandlerSync(crossinline operation: () -> TResult) {
        try {
            beforeOperation()
            val result = operation()
            handleResult(result)
        } catch (ex: Throwable) {
            if (exceptionClass.isInstance(ex))
                @Suppress("UNCHECKED_CAST")
                handleException(ex as TException)
            else handleUnknown(ex)
        } finally {
            afterOperation()
        }
    }
}