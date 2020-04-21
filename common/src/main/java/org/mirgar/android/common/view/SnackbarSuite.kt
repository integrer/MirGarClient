package org.mirgar.android.common.view

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarSuite(
    private val titleFactory: ContextTextGenerator,
    private val snackbarActionSuite: SnackbarActionSuite?
) {
    private var used = false

    fun use(view: View, timeLength: Int) {
        if (!used) {
            Snackbar.make(view, titleFactory.getText(view.context), timeLength)
                .also { snackbarActionSuite?.use(it) }
                .show()
            used = true
        }
    }

    fun mapTitle(block: (CharSequence) -> CharSequence): SnackbarSuite {
        return SnackbarSuite(
            object : ContextTextGenerator {
                override fun getText(context: Context) = block(titleFactory.getText(context))
            },
            snackbarActionSuite
        )
    }
}