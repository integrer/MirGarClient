package org.mirgar.android.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

abstract class PollViewModel<out ID, OptID> : ViewModel() {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val showResults: LiveData<Boolean>
    abstract val canVote: LiveData<Boolean>

    val options by lazy { rawOptions.map { if (showResults.value == true) normalize(it) else it} }

    protected abstract val rawOptions: LiveData<Collection<PollOption<OptID>>>

    companion object {
        protected fun <OptID> normalize(options: Collection<PollOption<OptID>>): Collection<PollOption<OptID>> {
            val optsSeq = options.asSequence()
            val totalVotes by lazy { optsSeq.map { it.votes.toDouble() }.sum() }

            val normalizedSeq = optsSeq.map { it.normalizeBy(totalVotes) }

            return ArrayList<PollOption<OptID>>(options.size).apply { addAll(normalizedSeq) }
        }

        private class NormalizedPollOption<out ID>(
            override val id: ID, override val name: CharSequence, override val votes: Double
        ) : PollOption<ID>()

        private fun <OptID> PollOption<OptID>.normalizeBy(total: Double) =
            NormalizedPollOption(id, name, votes.toDouble() / total)
    }
}

abstract class PollOption<out ID> {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val votes: Number
}
