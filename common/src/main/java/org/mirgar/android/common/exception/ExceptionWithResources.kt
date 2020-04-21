package org.mirgar.android.common.exception

import android.content.Context
import org.mirgar.android.common.view.ContextTextGenerator

open class ExceptionWithResources(
    private val resourceFactory: Context.() -> CharSequence
) : RuntimeException(), ContextTextGenerator {

    fun getLocalizedMessage(context: Context): CharSequence {
        return context.resourceFactory()
    }

    override fun getText(context: Context): CharSequence = getLocalizedMessage(context)
}