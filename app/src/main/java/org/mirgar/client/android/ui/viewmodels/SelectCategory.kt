package org.mirgar.client.android.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.mirgar.client.android.data.UnitOfWork
import org.mirgar.client.android.data.entity.CategoryWithStatus
import org.mirgar.client.android.ui.adapters.SelectCategoryAdapter
import kotlin.properties.Delegates

class SelectCategory(private val unitOfWork: UnitOfWork) : ViewModel() {

    val parentId = MutableLiveData<Long?>()

    lateinit var collection: LiveData<List<CategoryWithStatus>>
    var appealId by Delegates.notNull<Long>()

    lateinit var adapter: SelectCategoryAdapter
    private var actualCategoryId: Long? = null

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setup(appealId: Long, owner: LifecycleOwner) {
        this.appealId = appealId
        parentId.observe(owner) {newParentId ->
            if (this::collection.isInitialized) collection.removeObservers(owner)
            collection = unitOfWork.categoryRepository.childrenWithStatus(newParentId, appealId)
            collection.observe(owner) { result ->
                _isLoading.value = true
                adapter.submitList(result.map { SelectableCategory(it, this) }) {
                    _isLoading.value = false
                }
            }
        }
        viewModelScope.launch {
            actualCategoryId = unitOfWork.appealRepository.categoryIdOf(appealId)
            parentId.value = actualCategoryId?.let { unitOfWork.categoryRepository.superIdOf(it) }
        }
    }

    private val mutex = Mutex()
    fun setCategoryId(categoryId: Long? = null) {
        viewModelScope.launch {
            mutex.withLock {
                if (categoryId != actualCategoryId) {
                    unitOfWork.appealRepository.setCategory(appealId, categoryId)
                    actualCategoryId = categoryId
                }
            }
        }
    }
}