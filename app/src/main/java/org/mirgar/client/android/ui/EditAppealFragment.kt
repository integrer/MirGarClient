package org.mirgar.client.android.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mirgar.client.android.R
import org.mirgar.client.android.ui.viewmodels.EditAppealViewModel

class EditAppealFragment : Fragment() {

    companion object {
        fun newInstance() = EditAppealFragment()
    }

    private lateinit var viewModel: EditAppealViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_appeal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditAppealViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
