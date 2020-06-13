package org.mirgar.android.cryptoelection.ui

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import org.mirgar.android.cryptoelection.R
import org.mirgar.android.cryptoelection.databinding.DialogRegisteredBinding as Binding

class RegisteredDialog(secretKey: String, signIn: () -> Unit, ctx: Context, inflater: LayoutInflater) :
    AlertDialog(ctx) {
    init {
        val binding = Binding.inflate(inflater)
        binding.hexSecretKey = secretKey
        binding.copy.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("simple text", secretKey)
            clipboard.setPrimaryClip(clipData)
        }
        setView(binding.root)
        setButton(DialogInterface.BUTTON_POSITIVE, ctx.getText(R.string.sign_in)) { _, _ -> signIn() }
    }
}
