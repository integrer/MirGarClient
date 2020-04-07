package org.mirgar.client.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import org.mirgar.client.android.ui.adapters.AppealAdapter
import org.mirgar.client.android.ui.viewmodels.MyAppealsViewModel
import org.mirgar.client.android.ui.viewmodels.viewModelFactory
import org.mirgar.client.android.databinding.MyAppealsFragmentBinding as Binding

class MyAppealsFragment : Fragment() {

    private lateinit var binding: Binding

    private val viewModel: MyAppealsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Binding.inflate(inflater, container, false)

        val adapter = AppealAdapter()
        binding.appealsList.adapter = adapter

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: AppealAdapter) {
        viewModel.appealsWithCategoryTitles.observe(viewLifecycleOwner) { result ->
            binding.hasAppeals = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }
}
