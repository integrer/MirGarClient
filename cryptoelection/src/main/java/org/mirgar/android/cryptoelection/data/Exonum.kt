package org.mirgar.android.cryptoelection.data

import java.time.Duration

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

import com.exonum.binding.common.crypto.AbstractEd25519CryptoFunction
import com.exonum.binding.common.crypto.CryptoFunction
import com.exonum.binding.common.crypto.CryptoFunctions
import com.exonum.client.ExonumClient
import com.exonum.client.response.ServiceInstanceInfo
import com.goterl.lazycode.lazysodium.LazySodium
import com.goterl.lazycode.lazysodium.LazySodiumAndroid
import com.goterl.lazycode.lazysodium.SodiumAndroid
import com.goterl.lazycode.lazysodium.utils.LibraryLoader
import com.goterl.lazycode.lazysodium.utils.LibraryLoadingException

import org.mirgar.android.cryptoelection.failure.CommonFailure

class HostConfiguration(
    host: CharSequence,
    port: Int? = null,
    useSsl: Boolean
) {
    val url = "${if (useSsl) "https" else "http"}://$host${port?.let { ":$it" } ?: ""}"
}

class GettingTransactionConfiguration(
    val attempts: Int,
    val delay: Duration
)

class ExonumServicesConfiguration(
    cryptoelectionServiceName: String,
    timeServiceName: String,
    private val client: ExonumClient
) {
    val cryptoelectionServiceInfo: ServiceInfo = ServiceInfoImpl(cryptoelectionServiceName)
    @Suppress("UNUSED")
    val timeServiceInfo: ServiceInfo = ServiceInfoImpl(timeServiceName)

    abstract class ServiceInfo {
        abstract val value: ServiceInstanceInfo?

        @Throws(CommonFailure.NoServiceInformation::class)
        fun require() = value ?: throw CommonFailure.NoServiceInformation()
    }

    private inner class ServiceInfoImpl(private val serviceName: String) : ServiceInfo() {
        override val value: ServiceInstanceInfo? by lazy {
            client.findServiceInfo(serviceName).orElse(null)
        }
    }
}

class ExonumKit(
    val gettingTransactionConfiguration: GettingTransactionConfiguration,
    val client: ExonumClient,
    val servicesConfiguration: ExonumServicesConfiguration
)

private class AndroidEd25519CryptoFunction : AbstractEd25519CryptoFunction() {

    private val lazySodium = LazySodiumAndroid(SodiumAndroid())

    override fun getLazySodium() = lazySodium
}

fun Kodein.Builder.bindExonum() {
    // Override crypto function to avoid application to fall. 'lazysodium-java' does not supports
    // android platform, but 'lazysodium-android' does, so using it instead.
    CryptoFunctions.setEd25519(AndroidEd25519CryptoFunction())
    bind<CryptoFunction>() with singleton { CryptoFunctions.ed25519() }

    bind<HostConfiguration>() with singleton {
        HostConfiguration(host = "10.0.2.2", port = 8008, useSsl = false)
    }

    bind<GettingTransactionConfiguration>() with singleton {
        GettingTransactionConfiguration(attempts = 10, delay = Duration.ofMillis(500))
    }

    bind<ExonumClient>() with provider {
        ExonumClient.newBuilder()
            .setExonumHost(instance<HostConfiguration>().url)
            .build()
    }

    bind<ExonumServicesConfiguration>() with provider {
        ExonumServicesConfiguration("crypto_election", "time", instance())
    }

    bind<ExonumKit>() with provider { ExonumKit(instance(), instance(), instance()) }
}
