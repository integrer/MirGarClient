package org.mirgar.android.mgclient.data

enum class AppealStatus {
    ACCEPTED {
        override val id = 1
    },
    PROCESSING {
        override val id = 7
    },
    PROCESSED {
        override val id = 2
    },
    DENIED {
        override val id = 3
    },
    UNKNOWN {
        override val id = -1
    };
    abstract val id: Int
    companion object {
        fun from(number: Int) = when(number) {
            ACCEPTED.id -> ACCEPTED
            PROCESSING.id -> PROCESSING
            PROCESSED.id -> PROCESSED
            DENIED.id -> DENIED
            else -> UNKNOWN
        }
    }
}