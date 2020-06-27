package org.mirgar.android.cryptoelection.viewmodel

import androidx.lifecycle.MutableLiveData
import org.mirgar.android.common.model.PollModel
import org.mirgar.android.common.viewmodel.PollViewModel as BasePollViewModel

class ElectionViewModel(override val id: String) : BasePollViewModel<String, Int>() {
    private val _model = MutableLiveData<PollModel<Int>>()
    override val model = _model

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun vote(optionId: Int) {
        TODO("Not yet implemented")
    }
}