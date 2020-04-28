package org.mirgar.android.common.util

class Event<T>(val value: T) {
    private var used = false

    val unused
        get() = if (!used) {
            used = true
            value
        } else null
}