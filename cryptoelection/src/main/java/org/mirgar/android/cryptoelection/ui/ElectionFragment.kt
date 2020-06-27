package org.mirgar.android.cryptoelection.ui

import androidx.lifecycle.ViewModelProvider
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.mirgar.android.common.ui.PollFragment as BasePollFragment

class ElectionFragment : BasePollFragment<String, Int>(), KodeinAware {
    override val kodein by closestKodein()

    override val viewModelFactory: ViewModelProvider.Factory
        get() = TODO("Not yet implemented")
}
