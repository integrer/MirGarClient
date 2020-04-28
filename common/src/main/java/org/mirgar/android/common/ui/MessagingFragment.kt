package org.mirgar.android.common.ui

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.map
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import org.mirgar.android.common.R
import org.mirgar.android.common.util.with
import org.mirgar.android.common.viewmodel.MessagingViewModel

abstract class MessagingFragment : Fragment {
    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected abstract val viewModel: MessagingViewModel

    protected fun attach(viewModel: MessagingViewModel) {
        val view = view ?: return
        fun withErrorPrefix(msg: CharSequence) = "${view.context.getText(R.string.error)}: $msg"
        (viewModel.errorChannel.map { it setTitle ::withErrorPrefix } with viewModel.messageChannel)
            .observe(viewLifecycleOwner) { it.use(view, Snackbar.LENGTH_LONG) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
    }

    private fun setupSnackbar() {
        attach(viewModel)
    }
}

