package org.mirgar.client.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

import org.mirgar.client.android.R
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.ui.viewmodels.MyAppealsViewModel

class MyAppealsFragment : Fragment() {

    private val unitOfWork: UnitOfWork by lazy { UnitOfWork(requireContext()) }

    private val viewModel: MyAppealsViewModel by viewModels {
        MyAppealsViewModel.Factory(unitOfWork)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_appeals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
