package org.mirgar.android.cryptoelection.model

import org.mirgar.android.common.model.PollModel

data class PollGroupModel<TPollId, TOptId>(
    val name: CharSequence,
    val polls: List<IdentifiedPollModel<TPollId, TOptId>>
)

data class IdentifiedPollModel<TPollId, TOptId>(
    val id: TPollId,
    val body: PollModel<TOptId>
)