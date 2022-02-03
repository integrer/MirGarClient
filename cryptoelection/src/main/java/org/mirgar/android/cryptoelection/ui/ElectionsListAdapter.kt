package org.mirgar.android.cryptoelection.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mirgar.android.cryptoelection.model.IdentifiedPollModel
import org.mirgar.android.cryptoelection.viewmodel.ElectionsListViewModel
import org.mirgar.android.cryptoelection.databinding.FragmentElectionItemBinding as ItemBinding

private typealias PollModel = IdentifiedPollModel<String, Int>

// TODO: Implement hierarchical view
class ElectionsListAdapter(
    private val parentViewModel: ElectionsListViewModel, private val lifecycleOwner: LifecycleOwner
) : ListAdapter<PollModel, ElectionsListAdapter.ViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<PollModel>() {
        override fun areItemsTheSame(oldItem: PollModel, newItem: PollModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PollModel, newItem: PollModel) = oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PollModel) {
            binding.title = model.body.name as String?
            binding.setClick { parentViewModel.open(model.id) }
            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        parentViewModel.storage[item.id] = item
        holder.bind(item)
    }
}