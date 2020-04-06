package org.mirgar.client.android.ui.viewmodels

import org.mirgar.client.android.data.entity.AppealWithCategoryTitle

data class Appeal(private val inner: AppealWithCategoryTitle) {
    val appeal
        get() = inner.appeal

    val title
        get() = inner.appeal.title

    val categoryTitle
        get() = inner.categoryTitle
}