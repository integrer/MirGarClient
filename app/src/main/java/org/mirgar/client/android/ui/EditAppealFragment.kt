package org.mirgar.client.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import org.mirgar.client.android.ui.viewmodels.EditAppeal
import org.mirgar.client.android.ui.viewmodels.Factory
import org.mirgar.client.android.databinding.FragmentEditAppealBinding as Binding

class EditAppealFragment : Fragment() {

    private val viewModel: EditAppeal by viewModels { Factory(requireContext()) }

    private val args: EditAppealFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)
            .apply {
                viewmodel = viewModel
                lifecycleOwner = viewLifecycleOwner
//                selectCategory.setOnClickListener { v ->
//                    val i = appeal
//                }
            }

        viewModel.setup(args.appealId, viewLifecycleOwner)

        return binding.root
    }
}
