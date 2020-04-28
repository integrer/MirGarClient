package org.mirgar.android.common.util.messaging

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarSuite(
    private val titleFactory: ContextTextGenerator,
    private val snackbarActionSuite: SnackbarActionSuite?,
    private var used: Boolean = false
) {

    fun use(view: View, timeLength: Int) {
        if (!used) {
            Snackbar.make(view, titleFactory.getText(view.context), timeLength)
                .also { snackbarActionSuite?.use(it) }
                .show()
            used = true
        }
    }

    infix fun setTitle(block: (CharSequence) -> CharSequence): SnackbarSuite {
        return SnackbarSuite(titleFactory.map(block), snackbarActionSuite, used)
    }
}

class SnackbarActionSuite(
    private val titleFactory: ContextTextGenerator,
    private val block: (View) -> Unit
) {
    fun use(snackbar: Snackbar) {
        snackbar.setAction(titleFactory.getText(snackbar.context), block)
    }
}