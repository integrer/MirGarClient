package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import org.mirgar.android.common.util.Event

sealed class ImageItem : ViewModel() {
    abstract fun click()
    private val _isRegular get() = this is RegularImageItem

    val isRegular: LiveData<Boolean> = MutableLiveData(_isRegular)

    object ItemCallback : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean =
            when (oldItem) {
                is RegularImageItem -> newItem is RegularImageItem && oldItem.id == newItem.id
                is AddImageItem -> newItem is AddImageItem
            }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean =
            when (oldItem) {
                is RegularImageItem -> newItem is RegularImageItem && oldItem.id == newItem.id
                        && oldItem.uri == newItem.uri
                is AddImageItem -> newItem is AddImageItem
            }
    }


    companion object {
        @JvmStatic
        fun tryGetUri(item: ImageItem) =
            when (item) {
                is RegularImageItem -> item.uri
                is AddImageItem -> null
            }
    }
}

data class AddImageItem(private val channel: MutableLiveData<Event<Unit>>) : ImageItem() {
    override fun click() {
        channel.value = Event(Unit)
    }
}

data class RegularImageItem(
    val uri: String,
    val id: Long,
    private val channel: MutableLiveData<Event<Long>>
) : ImageItem() {
    override fun click() {
        channel.value = Event(id)
    }
}
