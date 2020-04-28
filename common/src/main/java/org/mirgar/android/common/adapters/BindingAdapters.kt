package org.mirgar.android.common.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.mirgar.android.common.R

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("tryLoadImageFrom", "orElse")
fun tryLoadImageFrom(view: ImageView, uri: String?, fallback: Drawable) {
    uri?.let { imageUri ->
        Glide.with(view)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(fallback)
            .into(view)
    } ?: view.setImageDrawable(fallback)
}

@BindingAdapter("onClick_actionConsequence", "onClick_ifConfirmed")
fun doIfConfirmBinding(view: View, consequence: CharSequence, action: Runnable) =
    view.setOnClickListener { doIfConfirm(view.context, consequence, action) }

/**
 * Shows confirmation prompt with specified [consequence]. Performs specified [action] only if
 * confirmed by user.
 */
fun doIfConfirm(context: Context, consequence: CharSequence, action: Runnable) {
    doIfConfirm(context, consequence) { action.run() }
}

fun doIfConfirm(context: Context, consequence: CharSequence, action: () -> Unit) {
    AlertDialog.Builder(context)
        .setMessage("$consequence\n${context.getString(R.string.continue_question)}")
        .setPositiveButton(android.R.string.yes) { _, _ -> action() }
        .setNegativeButton(android.R.string.cancel) { _, _ -> }
        .show()
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    view.adapter = adapter
}
