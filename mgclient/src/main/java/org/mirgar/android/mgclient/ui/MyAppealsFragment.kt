package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import org.mirgar.android.common.ui.MessagingFragment
import org.mirgar.android.mgclient.ui.adapters.AppealAdapter
import org.mirgar.android.mgclient.ui.adapters.navigateToEditAppeal
import org.mirgar.android.mgclient.ui.viewmodels.MyAppealsViewModel
import org.mirgar.android.mgclient.ui.viewmodels.viewModelFactory
import org.mirgar.android.mgclient.databinding.FragmentMyAppealsBinding as Binding

class MyAppealsFragment : MessagingFragment() {

    private lateinit var binding: Binding

    override val viewModel: MyAppealsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Binding.inflate(inflater, container, false)

        binding.isDataLoaded = false

        val adapter = AppealAdapter(requireContext())
        binding.appealsList.adapter = adapter

        binding.add.setOnClickListener { it.navigateToEditAppeal() }

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: AppealAdapter) {
        viewModel.appealsWithCategoryTitles.observe(viewLifecycleOwner) { result ->
            binding.isDataLoaded = false
            binding.hasAppeals = !result.isNullOrEmpty()
            adapter.submitList(result) { binding.isDataLoaded = true }
        }
    }
}
