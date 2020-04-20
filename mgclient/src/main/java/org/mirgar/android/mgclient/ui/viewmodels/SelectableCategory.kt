package org.mirgar.android.mgclient.ui.viewmodels

import androidx.recyclerview.widget.DiffUtil

import org.mirgar.android.mgclient.cfg.Joomla as JoomlaCfg
import org.mirgar.android.mgclient.data.models.CategoryWithStatus

data class SelectableCategory(
    private val category: CategoryWithStatus,
    private val parentVM: SelectCategory
) : org.mirgar.android.common.viewmodel.SelectableItem() {

    val joomlaCfg by lazy { JoomlaCfg.current }

    override val name: String get() = category.category.title
    override val imageUri: String? get() = category.category.iconUrl?.let { joomlaCfg.hostUrl + it }
    override val isSelected: Boolean get() = category.isRelated

    private val appealId get() = category.appealId
    private val categoryId get() = category.category.id
    val hasSubcategories get() = category.hasSubcategories

    override fun select() {
        parentVM.setCategoryId(categoryId)
    }

    override fun deselect() {
        parentVM.setCategoryId()
    }

    override fun click() {
        parentVM.setSuper(category)
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