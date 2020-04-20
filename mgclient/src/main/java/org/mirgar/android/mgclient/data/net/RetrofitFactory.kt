package org.mirgar.android.mgclient.data.net

import okhttp3.OkHttpClient
import org.mirgar.android.mgclient.utils.converter.JacksonBsonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import org.mirgar.android.mgclient.cfg.Joomla as JoomlaCfg

class RetrofitFactory {
    private fun getWithJson(): Retrofit = with(JoomlaCfg.current) {
        Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(getClient("json"))
            .build()
    }

    private fun getWithBson(): Retrofit = with(JoomlaCfg.current) {
        Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(JacksonBsonConverterFactory.create())
            .client(getClient("bson"))
            .build()
    }

    private fun getClient(type: String) = OkHttpClient.Builder().addInterceptor { chain ->
        chain.request().let { original ->
            original.newBuilder()
                .header("Accept", "application/${type}")
                .method(original.method(), original.body())
                .build()
        }.let { request ->
            chain.proceed(request)
        }
    }
        .build()

    val default get() = getWithBson()
}