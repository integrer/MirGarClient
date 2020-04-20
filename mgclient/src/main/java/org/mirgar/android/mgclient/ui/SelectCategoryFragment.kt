package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.*
import androidx.lifecycle.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.ui.adapters.SelectCategoryAdapter
import org.mirgar.android.mgclient.databinding.FragmentSelectCategoryBinding as Binding
import org.mirgar.android.mgclient.ui.viewmodels.SelectCategory as ViewModel
import org.mirgar.android.mgclient.ui.viewmodels.viewModelFactory
import java.lang.ref.WeakReference

class SelectCategoryFragment : Fragment() {
    private val vm: ViewModel by viewModels { viewModelFactory }

    private val args: SelectCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        vm.adapter = SelectCategoryAdapter()

        with(binding) {
            viewModel = vm
            lifecycleOwner = viewLifecycleOwner
            categoriesList.adapter = vm.adapter
        }

        vm.setup(args.appealId, viewLifecycleOwner)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.select_category_fragment_menu, menu)

        val levelUpItem = WeakReference(menu.findItem(R.id.level_up))

        vm.canMoveUp.observe(viewLifecycleOwner) { canMoveUp ->
            levelUpItem.get()?.apply { isEnabled = canMoveUp }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.level_up -> {
                vm.levelUp()
                true
            }
            else -> false
        }
    }
}