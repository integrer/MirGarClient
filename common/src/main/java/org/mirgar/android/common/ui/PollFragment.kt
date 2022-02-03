package org.mirgar.android.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import org.mirgar.android.common.adapters.PollAdapter
import org.mirgar.android.common.viewmodel.PollViewModel
import org.mirgar.android.common.databinding.FragmentPollBinding as Binding

abstract class PollFragment<TId, TOptId> : Fragment() {
    protected abstract val viewModel: PollViewModel<TId, TOptId>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.name = viewModel.name
        viewModel.canVote.observe(viewLifecycleOwner) {}
        viewModel.showResults.observe(viewLifecycleOwner) {}

        binding.list.adapter = PollAdapter(viewModel, viewLifecycleOwner).apply {
            viewModel.options.observe(viewLifecycleOwner, ::submitList)
        }

        return binding.root
    }
}