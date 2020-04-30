package org.mirgar.android.mgclient.ui.adapters

import android.view.View
import androidx.navigation.findNavController

import org.mirgar.android.mgclient.data.entity.Appeal
import org.mirgar.android.mgclient.ui.MyAppealsFragmentDirections

fun View.navigateToEditAppeal(appeal: Appeal) {
    navigateToEditAppeal(appeal.id)
}

fun View.navigateToEditAppeal(appealId: Long? = null) {
    val direction = MyAppealsFragmentDirections.actionNavigationMyAppealsToEditAppealFragment(
        appealId ?: 0L,
        appealId != null
    )
    findNavController().navigate(direction)
}
