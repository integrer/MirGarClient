package org.mirgar.android.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mirgar.android.common.viewmodel.PollOption
import org.mirgar.android.common.viewmodel.PollViewModel
import org.mirgar.android.common.databinding.FragmentPollOptionBinding as Binding


class PollAdapter<out ID, OptID>(private val parentVM: PollViewModel<ID, OptID>) :
    ListAdapter<PollOption<OptID>, PollAdapter<ID, OptID>.ViewHolder>(ItemCallback()) {

    private class ItemCallback<OptID> : DiffUtil.ItemCallback<PollOption<OptID>>() {
        override fun areItemsTheSame(
            oldItem: PollOption<OptID>,
            newItem: PollOption<OptID>
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PollOption<OptID>,
            newItem: PollOption<OptID>
        ): Boolean = oldItem == newItem
    }

    inner class ViewHolder(private val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PollOption<OptID>) {
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