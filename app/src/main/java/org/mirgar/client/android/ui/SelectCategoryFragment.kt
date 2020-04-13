package org.mirgar.client.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import org.mirgar.client.android.ui.adapters.SelectCategoryAdapter
import org.mirgar.client.android.databinding.FragmentSelectCategoryBinding as Binding
import org.mirgar.client.android.ui.viewmodels.SelectCategory as ViewModel
import org.mirgar.client.android.ui.viewmodels.viewModelFactory

class SelectCategoryFragment : Fragment() {
    private val viewModel: ViewModel by viewModels { viewModelFactory }

    private val args: SelectCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        viewModel.adapter = SelectCategoryAdapter(requireContext())

        viewModel.setup(args.appealId, viewLifecycleOwner)

        return binding.root
    }
}