package org.mirgar.android.cryptoelection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.mirgar.android.cryptoelection.R
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.operations.DelegatedOperationHandler
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.viewmodel.AuthorizationViewModel
import org.mirgar.android.cryptoelection.viewmodel.RegistrationViewModel
import org.mirgar.android.cryptoelection.viewmodel.RegistrationViewModel.ValidationError
import org.mirgar.android.cryptoelection.viewmodel.kodeinVMFactory
import org.mirgar.android.cryptoelection.databinding.FragmentRegistrationBinding as Binding

class RegistrationFragment : Fragment(), KodeinAware {
    private val superKodein by closestKodein()

    override val kodein by Kodein.lazy {
        extend(superKodein, allowOverride = true)

        bind<BaseOperationHandler>(overrides = true) with provider {
            OperationHandler(superKodein.direct.instance()) // Explicit using parent instance
        }
    }

    private val vmFactory by lazy(::kodeinVMFactory)

    private val registrationViewModel: RegistrationViewModel by viewModels { vmFactory }
    private val authorizationViewModel: AuthorizationViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = registrationViewModel
        registrationViewModel.model.observe(viewLifecycleOwner) {}

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = requireContext()

        registrationViewModel.mapErr = {
            when (it) {
                ValidationError.EmptyField -> context.getString(R.string.field_required)
                ValidationError.InvalidEmail -> context.getString(R.string.invalid_email)
                ValidationError.InvalidPhone -> context.getString(R.string.invalid_phone)
            }
        }
    }

    private inner class OperationHandler(parent: BaseOperationHandler) :
        DelegatedOperationHandler(parent) {

        override fun handleResult(result: OperationResult) {
            if (result is OperationResult.Registered) {
                getScope().launch {
                    RegisteredDialog(
                        result.secretKey,
                        { authorizationViewModel.login(result.secretKey) },
                        requireContext(),
                        layoutInflater
                    ).show()
                }
            } else super.handleResult(result)
        }

        override fun getScope(): CoroutineScope = lifecycleScope

    }

}