package org.mirgar.android.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.mirgar.android.common.viewmodel.PollViewModel
import org.mirgar.android.common.databinding.FragmentPollBinding as Binding

abstract class PollFragment<out ID, OptID> : Fragment() {
    protected abstract val viewModelFactory: ViewModelProvider.Factory

    private val _viewModel by viewModels<PollViewModel<ID, OptID>> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.viewModel = _viewModel

        return binding.root
    }
}