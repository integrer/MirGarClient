package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Transformations
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.ui.adapters.SelectCategoryAdapter
import java.lang.ref.WeakReference
import org.mirgar.android.mgclient.databinding.FragmentSelectCategoryBinding as Binding
import org.mirgar.android.mgclient.ui.viewmodels.SelectCategory as ViewModel

class SelectCategoryFragment : UnitOfWorkHolderFragment() {
    override val viewModel: ViewModel by viewModels { viewModelFactory }

    private val args: SelectCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)

        viewModel.adapter = SelectCategoryAdapter()

        val externalVM = viewModel

        with(binding) {
            viewModel = externalVM
            lifecycleOwner = viewLifecycleOwner
            categoriesList.adapter = externalVM.adapter
        }

        viewModel.setup(args.appealId, viewLifecycleOwner)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.select_category_fragment_menu, menu)

        val levelUpItem = WeakReference(menu.findItem(R.id.level_up))

        Transformations.distinctUntilChanged(viewModel.canMoveUp)
            .observe(viewLifecycleOwner) { canMoveUp ->
                levelUpItem.get()?.isEnabled = canMoveUp
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.level_up -> {
                viewModel.levelUp()
                true
            }
            else -> false
        }
    }
}