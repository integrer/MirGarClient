package org.mirgar.android.cryptoelection.usecase

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import org.mirgar.android.cryptoelection.failure.CommonFailure
import org.mirgar.android.cryptoelection.failure.PermissionFailure
import org.mirgar.android.cryptoelection.operations.OperationResult
import java.net.ConnectException

abstract class InternetUseCase : AsyncUseCase {
    protected abstract val appContext: Context

    private fun checkAccessNetworkStatePermission() {
        if (ContextCompat.checkSelfPermission(
                appContext,
                android.Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED
        )
            throw PermissionFailure.LackAccessNetworkState()
    }

    private fun checkInternetPermission() {
        if (ContextCompat.checkSelfPermission(
                appContext,
                android.Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        )
            throw PermissionFailure.LackInternetPermission()
    }

    private fun checkInternetConnection() {
        val hasConnection = run {
            val connectivityManager =
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nw = connectivityManager.activeNetwork ?: return@run false
            val nwCap = connectivityManager.getNetworkCapabilities(nw) ?: return@run false

            nwCap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || nwCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    //for other device how are able to connect with Ethernet
                    || nwCap.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    //for check internet over Bluetooth
                    || nwCap.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        }

        if (!hasConnection) throw CommonFailure.NoInternetConnection()
    }

    protected abstract suspend fun run(): OperationResult

    override suspend operator fun invoke(): OperationResult {
        try {
            checkAccessNetworkStatePermission()
            checkInternetPermission()
            checkInternetConnection()
            return run()
        } catch (ex: RuntimeException) {
            when (val cause = ex.cause) {
                is ConnectException -> {
                    val phrase = "Failed to connect to "
                    val dest = cause.message?.takeIf { it.startsWith(phrase) }?.drop(phrase.length)
                    throw CommonFailure.UnableToConnect(dest)
                }
                else -> throw CommonFailure.Unknown()
            }
        }
    }
}