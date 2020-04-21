package org.mirgar.android.common.view

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.map
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import org.mirgar.android.common.R

abstract class BaseFragment : Fragment {
    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected abstract val viewModel: BaseViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.let { view ->
            val context = view.context
            viewModel.error
                .map { suite -> suite.mapTitle { msg -> "${context.getText(R.string.error)}: $msg" } }
                .combineWith(viewModel.message)
                .observe(this) { suite ->
                    suite.use(view, Snackbar.LENGTH_LONG)
                }
        }
    }
}

