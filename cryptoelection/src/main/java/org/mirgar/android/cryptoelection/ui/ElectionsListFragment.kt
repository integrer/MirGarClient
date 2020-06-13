package org.mirgar.android.cryptoelection.ui

import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

import org.mirgar.android.common.ui.MessagingFragment
import org.mirgar.android.cryptoelection.viewmodel.ElectionsListViewModel
import org.mirgar.android.cryptoelection.viewmodel.viewModels

class ElectionsListFragment : MessagingFragment(), KodeinAware {
    override val kodein by closestKodein()

    override val viewModel: ElectionsListViewModel by viewModels()
}
