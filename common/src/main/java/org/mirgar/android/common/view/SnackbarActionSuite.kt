package org.mirgar.android.common.view

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarActionSuite(
    private val titleFactory: ContextTextGenerator,
    private val block: (View) -> Unit
) {
    fun use(snackbar: Snackbar) {
        snackbar.setAction(titleFactory.getText(snackbar.context), block)
    }
}
