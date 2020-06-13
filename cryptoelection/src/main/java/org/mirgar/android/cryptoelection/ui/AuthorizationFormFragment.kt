package org.mirgar.android.cryptoelection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.mirgar.android.cryptoelection.viewmodel.AuthorizationViewModel
import org.mirgar.android.cryptoelection.viewmodel.viewModels
import org.mirgar.android.cryptoelection.databinding.FragmentAuthFormBinding as Binding

class AuthorizationFormFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.submit.setOnClickListener { binding.secretKey?.let(viewModel::login) }

        return binding.root
    }
}