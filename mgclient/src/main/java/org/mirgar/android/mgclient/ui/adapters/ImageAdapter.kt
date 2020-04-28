package org.mirgar.android.mgclient.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.ui.viewmodels.ImageItem
import org.mirgar.android.mgclient.databinding.ImageListItemBinding as Binding

class ImageAdapter : ListAdapter<ImageItem, ImageAdapter.ViewHolder>(ImageItem.ItemCallback) {
    class ViewHolder(private val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ImageItem) {
            with(binding) {
                this.viewModel = viewModel
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.image_list_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}