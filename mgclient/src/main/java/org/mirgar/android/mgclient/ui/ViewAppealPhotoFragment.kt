package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.mirgar.android.mgclient.ui.viewmodels.ViewAppealPhoto
import org.mirgar.android.mgclient.databinding.FragmentViewAppealPhotoBinding as Binding

class ViewAppealPhotoFragment : UnitOfWorkHolderFragment() {
    override val viewModel: ViewAppealPhoto by viewModels { viewModelFactory }

    private val args: ViewAppealPhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init(args.photoId)
        viewModel.goBack = { findNavController().navigateUp() }
        val binding = Binding.inflate(inflater, container, false).apply {
            viewModel = this@ViewAppealPhotoFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

}