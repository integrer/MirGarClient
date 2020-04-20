package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.mirgar.android.mgclient.ui.viewmodels.Authorization
import org.mirgar.android.mgclient.ui.viewmodels.viewModelFactory
import org.mirgar.android.mgclient.databinding.FragmentAuthorizationBinding as Binding

class AuthorizationFragment : Fragment() {

    private val viewModel: Authorization by viewModels { viewModelFactory }

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