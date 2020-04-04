package org.mirgar.client.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import org.mirgar.client.android.R
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.databinding.MyAppealsFragmentBinding as Binding
import org.mirgar.client.android.ui.viewmodels.MyAppealsViewModel

class MyAppealsFragment : Fragment() {

    //private lateinit var binding: FragmentMyAppealsBinging

    private val unitOfWork: UnitOfWork by lazy { UnitOfWork(requireContext()) }

    private val viewModel: MyAppealsViewModel by viewModels {
        MyAppealsViewModel.Factory(unitOfWork)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.setOnAdd { v ->
            val appealId = unitOfWork.appealRepository.new()
        }

        binding.hasAppeals = viewModel.hasMyAppeals

        return inflater.inflate(R.layout.my_appeals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
