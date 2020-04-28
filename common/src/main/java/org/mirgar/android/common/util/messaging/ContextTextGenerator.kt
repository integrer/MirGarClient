package org.mirgar.android.common.util.messaging

import android.content.Context

interface ContextTextGenerator {
    fun getText(context: Context): CharSequence
}

fun ContextTextGenerator.map(block: (CharSequence) -> CharSequence): ContextTextGenerator =
    ContextTextGenerator_Map(this, block)

private class ContextTextGenerator_Map(
    private val inner: ContextTextGenerator,
    private val block: (CharSequence) -> CharSequence
) : ContextTextGenerator {
    override fun getText(context: Context) = block(inner.getText(context))
}
