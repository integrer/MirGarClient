package org.mirgar.client.android.ui.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("tryLoadImageFrom")
fun tryLoadImageFrom(view: ImageView, uri: String?) {
    uri?.let { imageUri ->
        Glide.with(view)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
}
