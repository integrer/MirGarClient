package org.mirgar.android.mgclient.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.mirgar.android.common.adapters.doIfConfirm
import org.mirgar.android.common.ui.AsyncActivity
import org.mirgar.android.mgclient.R
import java.lang.ref.WeakReference
import org.mirgar.android.mgclient.databinding.FragmentAppealDetailsBinding as Binding
import org.mirgar.android.mgclient.ui.viewmodels.AppealDetailsViewModel as ItsViewModel

class AppealDetailsFragment : UnitOfWorkHolderFragment() {

    override val viewModel: ItsViewModel by viewModels { viewModelFactory }

    private val args: AppealDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.imageListViewModel.deferredActionResultFactory =
            (requireActivity() as AsyncActivity)::launchIntent

        viewModel.categoryNameModifier = { it ?: requireContext().getString(R.string.no_category) }

        val binding = Binding.inflate(inflater, container, false)
            .apply {
                viewmodel = viewModel
                lifecycleOwner = viewLifecycleOwner
                imageListViewModel = viewModel.imageListViewModel
                setSelectCategoryListener {
                    val direction = AppealDetailsFragmentDirections
                        .actionDstEditAppealToDstFragmentSelectCategory(viewModel.id.value!!)
                    findNavController().navigate(direction)
                }
            }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val appealId = args.appealId.takeIf { args.hasAppealId }

        viewModel.setup(appealId, viewLifecycleOwner)
        appealId?.let { viewModel.imageListViewModel.init(it, viewLifecycleOwner) }

        viewModel.goToAuthorization = {
            val direction =
                AppealDetailsFragmentDirections.actionDstEditAppealToDstFragmentAuthorization()
            findNavController().navigate(direction)
        }
        viewModel.goBack = { findNavController().navigateUp() }

        viewModel.imageListViewModel.apply {
            attach(this)
            detailsEventChannel.observe(viewLifecycleOwner) { evt ->
                evt.unused?.let { imageId ->
                    val direction = AppealDetailsFragmentDirections
                        .actionDstEditAppealToViewAppealPhotoFragment(imageId)
                    findNavController().navigate(direction)
                }
            }
        }
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
