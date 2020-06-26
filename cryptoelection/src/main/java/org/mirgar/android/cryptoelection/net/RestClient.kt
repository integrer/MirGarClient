package org.mirgar.android.cryptoelection.net

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.mirgar.android.cryptoelection.data.HostConfiguration
import org.mirgar.android.cryptoelection.net.models.ElectionGroup
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

interface RestClient {
    @GET("/api/services/crypto_election/v1/elections/suggested-for?key={key}")
    fun getSuggestedElections(@Path("key") key: String): Call<List<ElectionGroup>>

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