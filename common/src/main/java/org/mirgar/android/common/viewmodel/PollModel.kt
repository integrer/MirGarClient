package org.mirgar.android.common.viewmodel

abstract class PollModel<out ID, out OptID> {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val showResults: Boolean
    abstract val canVote: Boolean
    abstract val options: Collection<PollOption<OptID>>

    fun asNormalized() = this as? NormalizedPollModel ?: NormalizedPollModel.from(this)
}

class NormalizedPollModel<out ID, out OptID> internal constructor(
    override val id: ID,
    override val name: CharSequence,
    override val showResults: Boolean,
    override val canVote: Boolean,
    options: Collection<PollOption<OptID>>
) : PollModel<ID, OptID>() {
    override val options: Collection<NormalizedPollOption<OptID>>

    class NormalizedPollOption<out ID> internal constructor(
        override val id: ID, override val name: CharSequence, override val votes: Double
    ) : PollOption<ID>()

    private fun PollOption<OptID>.normalizeBy(total: Double) =
        NormalizedPollOption(id, name, votes.toDouble() / total)

    init {
        val optsSeq = options.asSequence()
        val totalVotes by lazy { optsSeq.map { it.votes.toDouble() }.sum() }

        val normalizedSeq = optsSeq.map { it.normalizeBy(totalVotes) }

        this.options =
            ArrayList<NormalizedPollOption<OptID>>(options.size).apply { addAll(normalizedSeq) }
    }

    companion object {
        fun <ID, OptID> from(o: PollModel<ID, OptID>) =
            NormalizedPollModel(o.id, o.name, o.showResults, o.canVote, o.options)
    }
}

abstract class PollOption<out ID> {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val votes: Number
}
