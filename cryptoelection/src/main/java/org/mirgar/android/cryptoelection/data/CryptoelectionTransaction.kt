package org.mirgar.android.cryptoelection.data

enum class CryptoelectionTransaction {
    CreateParticipant {
        override val id = 0
    },
    CreateAdministration {
        override val id = 1
    },
    IssueElection {
        override val id = 2
    },
    Vote {
        override val id = 3
    },
    SubmitLocation {
        override val id = 4
    };
    abstract val id: Int
}