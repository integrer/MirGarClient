package org.mirgar.android.common.viewmodel

import android.util.SparseArray
import androidx.core.util.getOrElse
import androidx.core.util.set
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import org.mirgar.android.common.data.tryMatchAsListOrConvert
import org.mirgar.android.common.model.PollModel
import org.mirgar.android.common.model.PollOptionModel

abstract class PollViewModel<TId, TOptId> : ViewModel() {
    abstract val id: TId
    val name by lazy { model.map { it.name } }
    val showResults by lazy { model.map { it.showResults } }
    val canVote by lazy { model.map { it.canVote } }

    var normalize = true
    var lazyNormalize = true

    protected open val shouldNormalize
        get() = normalize && (!lazyNormalize || showResults.value == true)

    val options by lazy<LiveData<List<PollOptionViewModel<TOptId>>>> {
        model.map { ChildList(it.optionModels, ::childViewModelFactory, shouldNormalize) }
    }

    protected abstract val model: LiveData<PollModel<TOptId>>

    abstract fun update()
    abstract fun vote(optionId: TOptId)
    protected open fun childViewModelFactory(model: PollOptionModel<TOptId>) =
        object : PollOptionViewModel<TOptId>(model) {
            override fun vote() = this@PollViewModel.vote(id)
        }

    protected class ChildList<TId>(
        input: Collection<PollOptionModel<TId>>,
        viewModelFactory: (PollOptionModel<TId>) -> PollOptionViewModel<TId>,
        shouldNormalize: Boolean
    ) : AbstractList<PollOptionViewModel<TId>>() {
        private val _input by lazy { input.tryMatchAsListOrConvert() }
        private val _results by lazy { SparseArray<PollOptionViewModel<TId>>(input.size) }
        private val _sum by lazy { input.asSequence().map { it.votes.toDouble() }.sum() }

        private val _viewModelFactory = if (shouldNormalize) {
            { viewModelFactory(it.normalizeBy(_sum)) }
        } else viewModelFactory

        override val size = input.size

        override fun get(index: Int) = _results.getOrElse(index) {
            _input[index].let(_viewModelFactory).also { _results[index] = it }
        }
    }
}

abstract class PollOptionViewModel<TId>(private val _model: PollOptionModel<TId>) {
    val id = _model.id
    val name = _model.name
    val votes = _model.votes

    abstract fun vote()

    open fun isSame(other: PollOptionViewModel<TId>) = id == other.id
    override fun equals(other: Any?) = _model == other
    override fun hashCode() = _model.hashCode()
}
