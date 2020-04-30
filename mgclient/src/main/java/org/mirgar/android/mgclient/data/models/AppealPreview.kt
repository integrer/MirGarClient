package org.mirgar.android.mgclient.data.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Embedded
import org.mirgar.android.mgclient.data.entity.Appeal

data class AppealPreview(
    @Embedded val appeal: Appeal,
    @ColumnInfo(name = "category_title") val categoryTitle: String?
) {
    object ItemCallback : DiffUtil.ItemCallback<AppealPreview>() {
        override fun areItemsTheSame(
            oldItem: AppealPreview,
            newItem: AppealPreview
        ) = oldItem.appeal.id == newItem.appeal.id

        override fun areContentsTheSame(
            oldItem: AppealPreview,
            newItem: AppealPreview
        ) = oldItem == newItem
    }
}