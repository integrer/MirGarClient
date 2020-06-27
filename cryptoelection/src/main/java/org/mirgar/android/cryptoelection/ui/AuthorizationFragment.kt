package org.mirgar.android.cryptoelection.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
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
import org.mirgar.android.cryptoelection.databinding.FragmentAuthorizationBinding as Binding

class AuthorizationFragment : Fragment(), KodeinAware {

    private val superKodein by closestKodein()

    override val kodein by Kodein.lazy {
        extend(superKodein, allowOverride = true)

        bind<BaseOperationHandler>(overrides = true) with provider {
            OperationHandler(superKodein.direct.instance()) // Explicit using parent instance
        }
    }

    private val isLoading by instance<MutableLiveData<Boolean>>(tag = "is-loading")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.tabs.adapter = Adapter()

        isLoading.value = false
        return binding.root
    }

    private inner class Adapter : FragmentStatePagerAdapter(
        childFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> RegistrationFragment()
            1 -> AuthorizationFormFragment()
            else -> throw IllegalStateException("Unknown position - $position")
        }

        val context = requireContext()

        override fun getPageTitle(position: Int) = when (position) {
            0 -> context.getString(R.string.sign_up)
            1 -> context.getString(R.string.sign_in)
            else -> throw IllegalStateException("Unknown position - $position")
        }
    }

    private inner class OperationHandler(parent: BaseOperationHandler) :
        DelegatedOperationHandler(parent) {

        override fun handleResult(result: OperationResult) {
            if (result is OperationResult.SignedIn) {
                Log.i("Auth", "OK")
                val dst = AuthorizationFragmentDirections.actionDstAuthorizationToDstElectionsList()
                findNavController().navigate(dst)
            } else super.handleResult(result)
        }

        override fun getScope(): CoroutineScope = lifecycleScope

    }
}