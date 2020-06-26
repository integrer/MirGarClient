package org.mirgar.android.cryptoelection.net

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.mirgar.android.cryptoelection.data.HostConfiguration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create

interface RestClient {
    private class Factory(val url: String) {
        fun makeRetrofit() = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        fun make() = makeRetrofit().create<RestClient>()
    }

    companion object {
        fun Kodein.Builder.bindRestClient() {
            bind<Factory>() with singleton { Factory(instance<HostConfiguration>().url) }

            bind<RestClient>() with provider { instance<Factory>().make() }
        }
    }
}