package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.ui.adapters.AppealAdapter
import org.mirgar.android.mgclient.ui.adapters.navigateToEditAppeal
import org.mirgar.android.mgclient.ui.viewmodels.MyAppealsViewModel
import java.lang.ref.WeakReference
import org.mirgar.android.mgclient.databinding.FragmentMyAppealsBinding as Binding

class MyAppealsFragment : UnitOfWorkHolderFragment() {

    private lateinit var binding: Binding

    private val appealsRepository by lazy { unitOfWork.appealRepository }

    override val viewModel: MyAppealsViewModel by viewModels { viewModelFactory }

    private val args: MyAppealsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init(args.ownOnly)

        binding = Binding.inflate(inflater, container, false)

        binding.isDataLoaded = false

        val adapter = AppealAdapter(requireContext())
        binding.appealsList.adapter = adapter

        binding.add.setOnClickListener { it.navigateToEditAppeal() }

        subscribeUi(adapter)

        setHasOptionsMenu(true)

        return binding.root
    }

    private lateinit var loginMenuItem: WeakReference<MenuItem>
    private lateinit var logoutMenuItem: WeakReference<MenuItem>

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appeal_list_menu, menu)

        loginMenuItem = WeakReference(menu.findItem(R.id.login))
        logoutMenuItem = WeakReference(menu.findItem(R.id.logout))

        val isLoginVisible = findNavController().currentDestination?.id == R.id.dst_my_appeals
                && !viewModel.isAuthorized
        loginMenuItem.get()?.apply { isVisible = isLoginVisible }
        logoutMenuItem.get()?.apply { isVisible = !isLoginVisible }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.login -> {
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.dst_my_appeals) {
                    navController.navigate(R.id.action_dst_my_appeals_to_dst_fragment_authorization)
                    true
                } else false
            }
            R.id.logout -> {
                unitOfWork.sharedPreferencesService.authority.unset()
                loginMenuItem.get()?.apply { isVisible = true }
                logoutMenuItem.get()?.apply { isVisible = false }
                true
            }
            R.id.update -> {
                appealsRepository.update(force = true)
                true
            }
            else -> false
        }
    }


    private fun subscribeUi(adapter: AppealAdapter) {
        viewModel.appealsWithCategoryTitles.observe(viewLifecycleOwner) { result ->
            binding.isDataLoaded = false
            binding.hasAppeals = !result.isNullOrEmpty()
            adapter.submitList(result) { binding.isDataLoaded = true }
        }
    }
}
