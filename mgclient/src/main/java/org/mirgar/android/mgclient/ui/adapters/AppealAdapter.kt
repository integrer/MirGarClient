package org.mirgar.android.mgclient.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.AppealStatus
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.AppealPreview
import org.mirgar.android.mgclient.ui.viewmodels.Appeal
import org.mirgar.android.mgclient.databinding.ListItemAppealBinding as Binding

class AppealAdapter(context: Context) : ListAdapter<AppealPreview, AppealAdapter.ViewHolder>(
    AppealPreview.ItemCallback
) {
    private val unitOfWork = UnitOfWork(context)

    class ViewHolder(private val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.body.setOnClickListener { v ->
                binding.viewModel?.appeal?.apply {
                    v.navigateToEditAppeal(this)
                }
            }
        }

        fun bind(appeal: AppealPreview, unitOfWork: UnitOfWork) {
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
