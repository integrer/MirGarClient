package org.mirgar.android.cryptoelection.failure

import com.exonum.messages.core.runtime.Errors

sealed class CryptoelectionFailure : TransactionFailure.GeneralCommitFailure() {
    protected object Codes {
        const val ParticipantAlreadyExists = 1
        const val AdministrationAlreadyExists = 2
        const val ParticipantNotFound = 3
        const val AdministrationNotFound = 4
        const val ElectionFinishedEarlierStart = 5
        const val ElectionNotFound = 6
        const val OptionNotFound = 7
        const val VotedYet = 8
        const val ElectionInactive = 9
        const val ElectionNotStartedYet = 10
        const val BadLocation = 11
    }

    object Resolver : GeneralExonumErrorResolver() {
        override fun resolve(ex: FailedToCommitRaw): GeneralCommitFailure =
            if (ex.rawError.kind == Errors.ErrorKind.SERVICE) {
                when (ex.rawError.code) {
                    Codes.ParticipantAlreadyExists -> ParticipantAlreadyExists(ex)
                    Codes.AdministrationAlreadyExists -> AdministrationAlreadyExists(ex)
                    Codes.ParticipantNotFound -> ParticipantNotFound(ex)
                    Codes.AdministrationNotFound -> AdministrationNotFound(ex)
                    Codes.ElectionFinishedEarlierStart -> ElectionFinishedEarlierStart(ex)
                    Codes.ElectionNotFound -> ElectionNotFound(ex)
                    Codes.OptionNotFound -> OptionNotFound(ex)
                    Codes.VotedYet -> VotedYet(ex)
                    Codes.ElectionInactive -> ElectionInactive(ex)
                    Codes.ElectionNotStartedYet -> ElectionNotStartedYet(ex)
                    Codes.BadLocation -> BadLocation(ex)
                    else -> UnknownError(ex, ex.rawError.description, ex.rawError.code)
                }
            } else super.resolve(ex)
    }

    abstract val errorCode: Int

    class ParticipantAlreadyExists(override val cause: Throwable? = null) :
        CryptoelectionFailure() {
        override val errorCode = Codes.ParticipantAlreadyExists
    }

    class AdministrationAlreadyExists(override val cause: Throwable? = null) :
        CryptoelectionFailure() {
        override val errorCode = Codes.AdministrationAlreadyExists
    }

    class ParticipantNotFound(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.ParticipantNotFound
    }

    class AdministrationNotFound(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.AdministrationNotFound
    }

    class ElectionFinishedEarlierStart(override val cause: Throwable? = null) :
        CryptoelectionFailure() {
        override val errorCode = Codes.ElectionFinishedEarlierStart
    }

    class ElectionNotFound(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.ElectionNotFound
    }

    class OptionNotFound(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.OptionNotFound
    }

    class VotedYet(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.VotedYet
    }

    class ElectionInactive(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.ElectionInactive
    }

    class ElectionNotStartedYet(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.ElectionNotStartedYet
    }

    class BadLocation(override val cause: Throwable? = null) : CryptoelectionFailure() {
        override val errorCode = Codes.BadLocation
    }

    class UnknownError(
        override val cause: Throwable? = null,
        override val message: String? = null,
        override val errorCode: Int
    ) : CryptoelectionFailure()
}
