package org.mirgar.android.cryptoelection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.mirgar.android.cryptoelection.R
import org.mirgar.android.cryptoelection.databinding.FragmentAuthorizationBinding as Binding

class AuthorizationFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        binding.tabs.adapter = Adapter()

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
}