package org.mirgar.android.mgclient.data.models

import androidx.room.Embedded
import org.mirgar.android.mgclient.data.entity.Appeal
import org.mirgar.android.mgclient.data.entity.Category

data class AppealWithCategory(
    @Embedded val appeal: Appeal,
    @Embedded val category: Category?
)