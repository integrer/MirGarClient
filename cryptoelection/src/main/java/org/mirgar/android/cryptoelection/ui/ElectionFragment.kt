package org.mirgar.android.cryptoelection.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.operations.DelegatedOperationHandler
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.viewmodel.ElectionViewModel
import org.mirgar.android.cryptoelection.viewmodel.kodeinVMFactory
import org.mirgar.android.common.ui.PollFragment as BasePollFragment

class ElectionFragment : BasePollFragment<String, Int>(), KodeinAware {
    val parentKodein by closestKodein()

    override val kodein by Kodein.lazy {
        extend(parentKodein, allowOverride = true)

        bind<BaseOperationHandler>(overrides = true) with provider {
            OperationHandler(parentKodein.direct.instance())
        }
    }

    private val args: ElectionFragmentArgs by navArgs()

    override val viewModel by viewModels<ElectionViewModel> { kodeinVMFactory(args.address) }

    private inner class OperationHandler(parent: BaseOperationHandler) :
        DelegatedOperationHandler(parent) {
        override fun handleResult(result: OperationResult) {
            if (result == OperationResult.Voted) {
                viewModel.updateVotes(true)
            } else super.handleResult(result)
        }
    }
}
