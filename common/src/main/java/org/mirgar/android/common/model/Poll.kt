package org.mirgar.android.common.model

data class PollModel<TOptId>(
    val name: CharSequence,
    val showResults: Boolean,
    val canVote: Boolean,
    val optionModels: Collection<PollOptionModel<TOptId>>
)

data class PollOptionModel<TId>(
    val id: TId,
    val name: CharSequence,
    val votes: Number
) {
    fun normalizeBy(total: Double) = PollOptionModel(id, name, votes.toDouble() / total)
}
