package org.mirgar.android.cryptoelection.usecase

import org.mirgar.android.cryptoelection.operations.OperationResult

interface UseCase {
    operator fun invoke(): OperationResult
}

interface AsyncUseCase {
    suspend operator fun invoke(): OperationResult
}
