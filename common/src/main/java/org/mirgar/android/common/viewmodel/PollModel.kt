package org.mirgar.android.common.viewmodel

abstract class PollModel<out ID> {
    abstract val name: CharSequence
    abstract val showResults: Boolean
    abstract val options: Collection<PollOption<ID>>

    fun asNormalized() = this as? NormalizedPollModel ?: NormalizedPollModel.from(this)
}

class NormalizedPollModel<out ID> internal constructor(
    override val name: CharSequence,
    override val showResults: Boolean,
    options: Collection<PollOption<ID>>
) : PollModel<ID>() {
    override val options: Collection<NormalizedPollOption<ID>>

    class NormalizedPollOption<out ID> internal constructor(
        override val id: ID, override val name: CharSequence, override val votes: Double
    ) : PollOption<ID>()

    private fun PollOption<ID>.normalizeBy(total: Double) =
        NormalizedPollOption(id, name, votes.toDouble() / total)

    init {
        val optsSeq = options.asSequence()
        val totalVotes by lazy { optsSeq.map { it.votes.toDouble() }.sum() }

        val normalizedSeq = optsSeq.map { it.normalizeBy(totalVotes) }

        this.options =
            ArrayList<NormalizedPollOption<ID>>(options.size).apply { addAll(normalizedSeq) }
    }

    companion object {
        fun <ID> from(other: PollModel<ID>) =
            NormalizedPollModel(other.name, other.showResults, other.options)
    }
}

abstract class PollOption<out ID> {
    abstract val id: ID
    abstract val name: CharSequence
    abstract val votes: Number
}
