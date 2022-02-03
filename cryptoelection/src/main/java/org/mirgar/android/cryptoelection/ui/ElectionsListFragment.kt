package org.mirgar.android.cryptoelection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.mirgar.android.common.ui.MessagingFragment
import org.mirgar.android.cryptoelection.databinding.FragmentElectionListBinding
import org.mirgar.android.cryptoelection.model.IdentifiedPollModel
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.operations.DelegatedOperationHandler
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.viewmodel.ElectionsListViewModel
import org.mirgar.android.cryptoelection.viewmodel.viewModels

class ElectionsListFragment : MessagingFragment(), KodeinAware {
    private val superKodein by closestKodein()

    override val kodein by Kodein.lazy {
        extend(superKodein, allowOverride = true)

        bind<BaseOperationHandler>(overrides = true) with provider {
            OperationHandler(superKodein.direct.instance()) // Explicit using parent instance
        }
    }

    override val viewModel: ElectionsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.navControllerFactory = this::findNavController

        val binding = FragmentElectionListBinding.inflate(inflater, container, false)
        binding.list.adapter = ElectionsListAdapter(viewModel, viewLifecycleOwner).also {
            listLiveData.observe(viewLifecycleOwner, it::submitList)
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        viewModel.load()
        return binding.root
    }

    private val listLiveData = MutableLiveData<List<IdentifiedPollModel<String, Int>>>()

    private inner class OperationHandler(parent: BaseOperationHandler) :
        DelegatedOperationHandler(parent) {
        override fun handleResult(result: OperationResult) {
            if (result is OperationResult.ElectionGroupsObtained) {
                listLiveData.postValue(result.value.flatMap { it.polls })
            } else super.handleResult(result)
        }
    }
}
