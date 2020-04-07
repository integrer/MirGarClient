package org.mirgar.client.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import org.mirgar.client.android.R
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.data.entity.AppealWithCategoryTitle
import org.mirgar.client.android.ui.viewmodels.Appeal
import org.mirgar.client.android.databinding.ListItemAppealBinding as Binding

class AppealAdapter(context: Context) : ListAdapter<AppealWithCategoryTitle, AppealAdapter.ViewHolder>(
    AppealWithCategoryTitle.ItemCallback
) {
    private val unitOfWork = UnitOfWork(context)

    class ViewHolder(private val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.body.setOnClickListener { v ->
                binding.viewModel?.appeal?.apply {
                    if (serverId == null) {
                        v.navigateToEditAppeal(this)
                    } else {
                        TODO("Not yet implemented")
                    }
                }
            }
        }

        fun bind(appeal: AppealWithCategoryTitle, unitOfWork: UnitOfWork) {
            with(binding) {
                viewModel = Appeal(appeal, unitOfWork)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_appeal,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), unitOfWork)
    }

}
