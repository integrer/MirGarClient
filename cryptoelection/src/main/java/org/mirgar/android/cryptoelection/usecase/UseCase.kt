package org.mirgar.android.cryptoelection.usecase

interface UseCase {
    operator fun invoke()
}

interface AsyncUseCase {
    suspend operator fun invoke()
}
