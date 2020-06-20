package org.mirgar.android.common.viewmodel

abstract class PollModel<out ID, out OptID> {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val showResults: Boolean
    abstract val canVote: Boolean
    abstract val options: Collection<PollOption<OptID>>

    fun asNormalized() = this as? NormalizedPollModel ?: NormalizedPollModel(this)
}

class NormalizedPollModel<out ID, out OptID> internal constructor(
    private val other: PollModel<ID, OptID>
) : PollModel<ID, OptID>() {
    override val id get() = other.id
    override val name get() = other.name
    override val showResults get() = other.showResults
    override val canVote get() = other.canVote

    override val options: Collection<NormalizedPollOption<OptID>> by lazy {
        val optsSeq = other.options.asSequence()
        val totalVotes by lazy { optsSeq.map { it.votes.toDouble() }.sum() }

        val normalizedSeq = optsSeq.map { it.normalizeBy(totalVotes) }

        ArrayList<NormalizedPollOption<OptID>>(other.options.size).apply { addAll(normalizedSeq) }
    }

    class NormalizedPollOption<out ID> internal constructor(
        override val id: ID, override val name: CharSequence, override val votes: Double
    ) : PollOption<ID>()

    private fun PollOption<OptID>.normalizeBy(total: Double) =
        NormalizedPollOption(id, name, votes.toDouble() / total)
}

abstract class PollOption<out ID> {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val votes: Number
}
