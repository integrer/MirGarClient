package org.mirgar.client.android.data.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Embedded

data class AppealWithCategoryTitle(
    @Embedded val appeal: Appeal,
    @ColumnInfo(name = "category_title") val categoryTitle: String?
) {
    object ItemCallback : DiffUtil.ItemCallback<AppealWithCategoryTitle>() {
        override fun areItemsTheSame(
            oldItem: AppealWithCategoryTitle,
            newItem: AppealWithCategoryTitle
        ) = oldItem.appeal.id == newItem.appeal.id

        override fun areContentsTheSame(
            oldItem: AppealWithCategoryTitle,
            newItem: AppealWithCategoryTitle
        ) = oldItem == newItem
    }
}