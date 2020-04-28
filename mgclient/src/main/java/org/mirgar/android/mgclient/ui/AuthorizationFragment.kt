package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.mirgar.android.common.ui.MessagingFragment
import org.mirgar.android.mgclient.ui.viewmodels.Authorization
import org.mirgar.android.mgclient.ui.viewmodels.viewModelFactory
import org.mirgar.android.mgclient.databinding.FragmentAuthorizationBinding as Binding

class AuthorizationFragment : MessagingFragment() {

    override val viewModel: Authorization by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.onAfterLogin = Runnable {
            findNavController().navigateUp()
        }

        val binding = Binding.inflate(inflater, container, false)
            .apply {
                viewModel = this@AuthorizationFragment.viewModel
                lifecycleOwner = viewLifecycleOwner
            }

        return binding.root
    }
}