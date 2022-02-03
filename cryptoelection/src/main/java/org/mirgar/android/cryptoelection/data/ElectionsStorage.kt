package org.mirgar.android.cryptoelection.data

import org.mirgar.android.cryptoelection.model.IdentifiedPollModel

class ElectionsStorage {
    private val data = HashMap<String, IdentifiedPollModel<String, Int>>()

    operator fun set(key: String, value: IdentifiedPollModel<String, Int>) {
        data[key] = value
    }

    operator fun get(key: String): IdentifiedPollModel<String, Int>? = data[key]

    fun clear() = data.clear()
}