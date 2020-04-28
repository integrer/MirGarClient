package org.mirgar.android.mgclient.ui.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.mirgar.android.common.viewmodel.MessagingViewModel
import org.mirgar.android.mgclient.R
import org.mirgar.android.mgclient.data.UnitOfWork
import org.mirgar.android.mgclient.data.models.CategoryWithStatus
import org.mirgar.android.mgclient.ui.adapters.SelectCategoryAdapter
import kotlin.properties.Delegates

class SelectCategory(unitOfWork: UnitOfWork) : MessagingViewModel() {

    private val superId = MutableLiveData<Long?>()

    val canMoveUp = superId.map { superId -> superId != null }

    private val categoryRepository = unitOfWork.categoryRepository
    private val appealRepository = unitOfWork.appealRepository

    private val collection = superId.distinctUntilChanged().switchMap {
        categoryRepository.childrenWithStatus(it, appealId)
    }
    var appealId by Delegates.notNull<Long>()

    lateinit var adapter: SelectCategoryAdapter
    private var actualCategoryId: Long? = null

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setup(appealId: Long, owner: LifecycleOwner) {
        this.appealId = appealId

        val vm = this

        fun observe() {
            collection.observe(owner) { result ->
                if (!_isLoading.value!!) _isLoading.value = true
                adapter.submitList(result.map { SelectableCategory(it, vm) }) {
                    _isLoading.value = false
                }
            }
        }

        viewModelScope.launch {
            try {
                actualCategoryId = appealRepository.categoryIdOf(appealId)
                superId.value = actualCategoryId?.let { categoryRepository.superIdOf(it) }
                categoryRepository.loadCategories()
            } finally {
                observe()
            }
        }
    }

    private val appealCategoryMutex = Mutex()
    fun setCategoryId(categoryId: Long? = null) {
        viewModelScope.launch {
            appealCategoryMutex.withLock {
                if (categoryId != actualCategoryId) {
                    appealRepository.setCategory(appealId, categoryId)
                    actualCategoryId = categoryId
                }
            }
        }
    }

    private val superIdMutex = Mutex()
    fun setSuper(category: CategoryWithStatus) {
        viewModelScope.launch {
            superIdMutex.withLock {
                if (category.hasSubcategories) {
                    superId.value = category.category.id
                } else _message.show(R.string.no_subcategories)
            }
        }
    }

    fun levelUp() {
        if (canMoveUp.value!!) {
            viewModelScope.launch {
                superId.value = categoryRepository.superIdOf(superId.value!!)
            }
        }
    }
}