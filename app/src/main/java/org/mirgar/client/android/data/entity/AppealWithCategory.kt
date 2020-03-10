package org.mirgar.client.android.data.entity

import androidx.room.Embedded

data class AppealWithCategory(
    @Embedded val appeal: Appeal,
    @Embedded val category: Category?
)