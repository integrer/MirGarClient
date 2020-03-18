package org.mirgar.client.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs

import org.mirgar.client.android.R
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.databinding.FragmentEditAppealBinding
import org.mirgar.client.android.ui.viewmodels.EditAppealViewModel

class EditAppealFragment : Fragment() {

    private val unitOfWork: UnitOfWork by lazy { UnitOfWork(requireContext()) }

    private val viewModel: EditAppealViewModel by viewModels {
        EditAppealViewModel.Factory(unitOfWork, args.appealId, this)
    }

    private val args: EditAppealFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditAppealBinding.inflate(inflater, container, false)
        context ?: return binding.root

        return inflater.inflate(R.layout.fragment_edit_appeal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
