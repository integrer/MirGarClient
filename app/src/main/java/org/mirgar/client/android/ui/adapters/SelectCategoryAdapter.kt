package org.mirgar.client.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mirgar.client.android.R

import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.ui.viewmodels.SelectableCategory
import org.mirgar.client.android.databinding.ListItemSelectableBinding as Binding

class SelectCategoryAdapter(context: Context) :
    ListAdapter<SelectableCategory, SelectCategoryAdapter.ViewHolder>(
        AsyncDifferConfig.Builder(SelectableCategory.ItemCallback).build()
    ) {

    class ViewHolder(private val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: SelectableCategory) {
            with(binding) {
                viewmodel = viewModel
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
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}