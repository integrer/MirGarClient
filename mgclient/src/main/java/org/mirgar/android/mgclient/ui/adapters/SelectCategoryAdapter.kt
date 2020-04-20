package org.mirgar.android.mgclient.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.ui.viewmodels.SelectableCategory
import kotlin.properties.Delegates
import org.mirgar.android.common.databinding.ListItemSelectableBinding as Binding

class SelectCategoryAdapter : ListAdapter<SelectableCategory, SelectCategoryAdapter.ViewHolder>(
    SelectableCategory.ItemCallback
) {
    private var colorAccent by Delegates.notNull<Int>()
    private var colorDefault by Delegates.notNull<Int>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val context = recyclerView.context
        colorAccent = ContextCompat.getColor(context, R.color.colorPrimary)
        colorDefault = ContextCompat.getColor(context, android.R.color.black)
    }

    class ViewHolder(
        private val binding: Binding,
        private val colorAccent: Int,
        private val colorDefault: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: SelectableCategory) {
            with(binding) {
                this.viewModel = viewModel

                title.setTextColor(if (viewModel.hasSubcategories) colorAccent else colorDefault)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_selectable,
                parent,
                false
            ),
            colorAccent,
            colorDefault
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}