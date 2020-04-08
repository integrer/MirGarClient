package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class SelectableItem : ViewModel() {
    abstract val name: String
    abstract val imageUri: String?
    abstract val isSelected: Boolean
    abstract fun select()
    abstract fun deselect()
    abstract fun click()
}