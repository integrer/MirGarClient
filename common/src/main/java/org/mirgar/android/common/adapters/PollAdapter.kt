package org.mirgar.android.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mirgar.android.common.viewmodel.PollOptionViewModel
import org.mirgar.android.common.viewmodel.PollViewModel
import org.mirgar.android.common.databinding.FragmentPollOptionBinding as Binding


class PollAdapter<out ID, OptID>(private val parentVM: PollViewModel<ID, OptID>) :
    ListAdapter<PollOptionViewModel<OptID>, PollAdapter<ID, OptID>.ViewHolder>(ItemCallback()) {

    private class ItemCallback<OptID> : DiffUtil.ItemCallback<PollOptionViewModel<OptID>>() {
        override fun areItemsTheSame(
            oldItem: PollOptionViewModel<OptID>,
            newItem: PollOptionViewModel<OptID>
        ): Boolean = oldItem.isSame(newItem)

        override fun areContentsTheSame(
            oldItem: PollOptionViewModel<OptID>,
            newItem: PollOptionViewModel<OptID>
        ): Boolean = oldItem == newItem
    }

    inner class ViewHolder(private val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PollOptionViewModel<OptID>) {
            binding.parentVM = parentVM
            binding.thisOption = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PollAdapter<ID, OptID>.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}