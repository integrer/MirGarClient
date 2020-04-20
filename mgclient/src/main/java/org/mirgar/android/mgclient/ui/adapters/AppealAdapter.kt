package org.mirgar.android.mgclient.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.AppealWithCategoryTitle
import org.mirgar.android.mgclient.ui.viewmodels.Appeal
import org.mirgar.android.mgclient.databinding.ListItemAppealBinding as Binding

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
