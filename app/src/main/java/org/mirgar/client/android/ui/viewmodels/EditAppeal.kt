package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.*

import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.data.entity.Appeal

class EditAppeal internal constructor(
    private val unitOfWork: UnitOfWork
) : MutableViewModel<Long>() {

    /**
     * Two-way data binding property for [Appeal.title]
     */
    val title = MutableLiveData<String>()

    /**
     * Two-way data binding property for [Appeal.description]
     */
    val description = MutableLiveData<String>()

    private var appealOriginal = Appeal()

    private val appealMerger: MediatorLiveData<Appeal> = with(MediatorLiveData<Appeal>()) {
        value = Appeal()
        addSource(title) { newTitle ->
            value?.let {
                if (it.title != newTitle) value = it.copy(title = newTitle)
            }
        }
        addSource(description) { newDescription ->
            value?.let {
                if (it.description != newDescription) value = it.copy(description = newDescription)
            }
        }
        this
    }

    private fun initBindings(appeal: Appeal) {
        appealMerger.value = appeal
        title.value = appeal.title
        description.value = appeal.description
    }

    //region Standard hooks
    override suspend fun init(id: Long, lifecycleOwner: LifecycleOwner) {
        unitOfWork.appealRepository.let { repo ->
            if (!repo.hasAppeal(id)) TODO("Throw custom exception")
            repo.getOne(id).observe(lifecycleOwner) {
                appealOriginal = it
            }
            appealOriginal = repo.getOneAsPlain(id)!!
            initBindings(appealOriginal)
        }
    }

    override suspend fun save(id: Long) {
        unitOfWork.appealRepository.save(appealMerger.value!!)
    }

    override suspend fun create() = unitOfWork.appealRepository.new(appealMerger.value!!)
    //endregion

    //region Standard properties
    override val observable = appealMerger

    override val isValueSame
        get() = appealMerger.value == if (isSaved) appealOriginal else Appeal.default
    //endregion
}
