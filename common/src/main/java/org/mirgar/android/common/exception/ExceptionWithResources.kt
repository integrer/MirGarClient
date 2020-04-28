package org.mirgar.android.common.exception

import android.content.Context
import org.mirgar.android.common.util.messaging.ContextTextGenerator

open class ExceptionWithResources(
    message: String? = null,
    cause: Throwable? = null,
    private val resourceFactory: Context.() -> CharSequence
) : RuntimeException(message, cause), ContextTextGenerator {

    fun getLocalizedMessage(context: Context): CharSequence {
        return context.resourceFactory()
    }

    override fun getText(context: Context) = getLocalizedMessage(context)
}