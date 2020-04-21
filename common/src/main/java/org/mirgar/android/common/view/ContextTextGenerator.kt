package org.mirgar.android.common.view

import android.content.Context

interface ContextTextGenerator {
    fun getText(context: Context): CharSequence
}