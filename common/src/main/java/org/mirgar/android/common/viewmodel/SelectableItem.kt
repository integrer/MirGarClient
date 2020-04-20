package org.mirgar.android.common.viewmodel

import androidx.lifecycle.ViewModel

abstract class SelectableItem : ViewModel() {
    /**
     * Name of item
     */
    abstract val name: String

    /**
     * Uri of icon image
     */
    abstract val imageUri: String?
    abstract val isSelected: Boolean
    abstract fun select()
    abstract fun deselect()
    abstract fun click()
}