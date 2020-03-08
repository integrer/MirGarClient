package org.mirgar.client.android.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mirgar.client.android.R

class MyAppealsFragment : Fragment() {

    companion object {
        fun newInstance() = MyAppealsFragment()
    }

    private lateinit var viewModel: MyAppealsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_appeals_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyAppealsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
