package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.data.entity.Appeal

class EditAppeal internal constructor(
    private val unitOfWork: UnitOfWork
) : MutableViewModel<Long>() {

    /**
     * Two-way data binding property for [Appeal.title]
     */
    val title = MutableLiveData<String>()

    private var appeal = Appeal()

    override suspend fun init(id: Long) {
        unitOfWork.appealRepository.getOneAsPlain(id)?.let { appeal ->
            this.appeal = appeal
            title.value = appeal.title
        }
    }

    override fun setObserversFor(owner: LifecycleOwner) {
        title.observe(owner, { appeal.title = it })
    }

    override suspend fun save(id: Long) {
        unitOfWork.appealRepository.save(appeal.withId(id))
    }

    override suspend fun create(): Long = unitOfWork.appealRepository.new(appeal)
}
