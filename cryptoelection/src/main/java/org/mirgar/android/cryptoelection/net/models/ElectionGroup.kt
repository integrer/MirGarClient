package org.mirgar.android.cryptoelection.net.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.mirgar.android.common.data.lazyMap
import org.mirgar.android.common.model.PollModel
import org.mirgar.android.common.model.PollOptionModel
import org.mirgar.android.cryptoelection.model.IdentifiedPollModel
import org.mirgar.android.cryptoelection.model.PollGroupModel
import java.util.*
import kotlin.properties.Delegates

class ElectionGroup {
    @JsonProperty("organization_name")
    lateinit var organizationName: String

    lateinit var elections: List<Election>

    fun toLocal() = PollGroupModel(
        organizationName,
        elections.lazyMap(Election::toLocal)
    )
}

class Election {
    lateinit var addr: String

    lateinit var name: String

    @Suppress("UNUSED")
    @JsonProperty("start_date")
    lateinit var startDate: Date

    @Suppress("UNUSED")
    @JsonProperty("finish_date")
    lateinit var finishDate: Date

    lateinit var options: List<ElectionOption>

    @delegate:JsonProperty("is_cancelled")
    var isCancelled by Delegates.notNull<Boolean>()

    @delegate:JsonProperty("is_voted_yet")
    var isVotedYet by Delegates.notNull<Boolean>()

    val showResults get() = isVotedYet

    val canVote get() = !isCancelled && !isVotedYet

    fun toLocal() = IdentifiedPollModel(
        addr, PollModel(name, showResults, canVote, options.lazyMap(ElectionOption::toLocal))
    )
}

class ElectionOption {
    var id by Delegates.notNull<Int>()

    lateinit var name: String

    @JsonProperty("votes_count")
    var votesCount: Int? = null

    fun toLocal() = PollOptionModel(id, name, votesCount ?: 0)
}
