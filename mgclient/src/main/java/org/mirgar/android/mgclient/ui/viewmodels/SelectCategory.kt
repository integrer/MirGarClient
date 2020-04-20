package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.CategoryWithStatus
import org.mirgar.android.mgclient.ui.adapters.SelectCategoryAdapter
import kotlin.properties.Delegates

class SelectCategory(private val unitOfWork: UnitOfWork) : ViewModel() {

    private val superId = MutableLiveData<Long?>()

    val canMoveUp = Transformations.map(superId) { superId -> superId != null }

    private lateinit var collection: LiveData<List<CategoryWithStatus>>
    var appealId by Delegates.notNull<Long>()

    lateinit var adapter: SelectCategoryAdapter
    private var actualCategoryId: Long? = null

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setup(appealId: Long, owner: LifecycleOwner) {
        this.appealId = appealId
        val vm = this

        superId.observe(owner) { newParentId ->
            viewModelScope.launch {
                if (vm::collection.isInitialized) collection.removeObservers(owner)
                collection = unitOfWork.categoryRepository.childrenWithStatus(newParentId, appealId)
                collection.observe(owner) { result ->
                    if (!_isLoading.value!!) _isLoading.value = true
                    adapter.submitList(result.map { SelectableCategory(it, vm) }) {
                        _isLoading.value = false
                    }
                }
            }
        }
        viewModelScope.launch {
            actualCategoryId = unitOfWork.appealRepository.categoryIdOf(appealId)
            superId.value = actualCategoryId?.let { unitOfWork.categoryRepository.superIdOf(it) }
        }
    }

    private val appealCategoryMutex = Mutex()
    fun setCategoryId(categoryId: Long? = null) {
        viewModelScope.launch {
            appealCategoryMutex.withLock {
                if (categoryId != actualCategoryId) {
                    unitOfWork.appealRepository.setCategory(appealId, categoryId)
                    actualCategoryId = categoryId
                }
            }
        }
    }

    private val superIdMutex = Mutex()
    fun setSuper(category: CategoryWithStatus) {
        viewModelScope.launch {
            superIdMutex.withLock {
                if (superId.value == category.category.id) return@launch
                if (category.hasSubcategories) {
                    superId.value = category.category.id
                    // TODO: Say to user that selected category has no subcategories
                }
            }
        }
    }

    fun levelUp() {
        if (superId.value != null) {
            viewModelScope.launch {
                superId.value = unitOfWork.categoryRepository.superIdOf(superId.value!!)
            }
        }
    }
}