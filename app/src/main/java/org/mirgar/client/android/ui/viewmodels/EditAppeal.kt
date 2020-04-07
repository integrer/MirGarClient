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
    private var appealOrig = Appeal()

    fun updateOriginal() {
        appealOrig = appeal.copy()
    }

    override suspend fun init(id: Long) {
        unitOfWork.appealRepository.getOneAsPlain(id)?.let { appeal ->
            this.appeal = appeal
            updateOriginal()
            title.value = appeal.title
        }
    }

    override fun setObserversFor(owner: LifecycleOwner) {
        title.observe(owner, {
            if (it != appeal.title) {
                appeal.title = it
                changed = appeal != appealOrig
            }
        })
    }

    override suspend fun save(id: Long) {
        unitOfWork.appealRepository.save(appeal)
        updateOriginal()
    }

    override suspend fun create(): Long = unitOfWork.appealRepository.new(appeal)
        .also {
            appeal = appeal.withId(it)
            updateOriginal()
        }
}
