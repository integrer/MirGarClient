package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.mirgar.android.common.adapters.doIfConfirm
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.ui.viewmodels.EditAppeal
import org.mirgar.android.mgclient.ui.viewmodels.viewModelFactory
import java.lang.ref.WeakReference
import org.mirgar.android.mgclient.databinding.FragmentEditAppealBinding as Binding

class EditAppealFragment : Fragment() {

    private val viewModel: EditAppeal by viewModels { viewModelFactory }

    private val args: EditAppealFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Binding.inflate(inflater, container, false)
            .apply {
                viewmodel = viewModel
                lifecycleOwner = viewLifecycleOwner
                selectCategory.setOnClickListener {
                    val direction = EditAppealFragmentDirections
                        .actionDstEditAppealToDstFragmentSelectCategory(viewModel.id.value!!)
                    findNavController().navigate(direction)
                }
            }

        val appealId = if (args.hasAppealId) args.appealId else null

        viewModel.setup(appealId, viewLifecycleOwner)

        viewModel.goToAuthorization = {
            val direction = EditAppealFragmentDirections
                .actionDstEditAppealToDstFragmentAuthorization()
            findNavController().navigate(direction)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_appeal_fragment_menu, menu)

        val sendItem = WeakReference(menu.findItem(R.id.send))

        val deleteItem = WeakReference(menu.findItem(R.id.delete))

        viewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            sendItem.get()?.apply { isEnabled = isSaved }
            deleteItem.get()?.apply { isEnabled = isSaved }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send -> {
                viewModel.send()
                true
            }
            R.id.delete -> {
                val ctx = requireContext()
                doIfConfirm(ctx, ctx.getString(R.string.consequence_appealDelete)) {
                    viewModel.delete()
                    findNavController().navigateUp()
                }
                true
            }
            else -> false
        }
    }
}
