package org.mirgar.client.android.ui.viewmodels

import androidx.recyclerview.widget.DiffUtil

import org.mirgar.client.android.data.entity.CategoryWithStatus

data class SelectableCategory(
    private val category: CategoryWithStatus,
    private val parentVM: SelectCategory
) : SelectableItem() {

    override val name: String get() = category.category.title
    override val imageUri: String? get() = category.category.iconUrl
    override val isSelected: Boolean get() = category.status

    private val appealId get() = category.appealId
    private val categoryId get() = category.category.id

    override fun select() {
        parentVM.setCategoryId(categoryId)
    }

    override fun deselect() {
        parentVM.setCategoryId()
    }

    override fun click() {
        parentVM.parentId.value = categoryId
    }

    companion object ItemCallback : DiffUtil.ItemCallback<SelectableCategory>() {
        override fun areItemsTheSame(
            oldItem: SelectableCategory,
            newItem: SelectableCategory
        ): Boolean =
            oldItem.appealId == newItem.appealId && oldItem.categoryId == newItem.categoryId

        override fun areContentsTheSame(
            oldItem: SelectableCategory,
            newItem: SelectableCategory
        ): Boolean = oldItem == newItem
    }
}