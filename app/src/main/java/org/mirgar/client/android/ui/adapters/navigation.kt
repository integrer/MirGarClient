package org.mirgar.client.android.ui.adapters

import android.view.View
import androidx.navigation.findNavController

import org.mirgar.client.android.data.entity.Appeal
import org.mirgar.client.android.ui.MyAppealsFragmentDirections

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
