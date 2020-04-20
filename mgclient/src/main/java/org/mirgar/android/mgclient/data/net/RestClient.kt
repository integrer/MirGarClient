package org.mirgar.android.mgclient.data.net

import org.mirgar.android.mgclient.data.net.models.Appeal
import org.mirgar.android.mgclient.data.net.models.AuthResponse
import retrofit2.Call

import org.mirgar.android.mgclient.data.net.models.CategoryContainer
import org.mirgar.android.mgclient.data.net.models.JoomlaAPIResponse
import retrofit2.http.*

interface RestClient {
    @GET("index.php?option=com_api&app=mirgar&resource=categories&format=raw")
    fun getAllCategories(): Call<JoomlaAPIResponse<CategoryContainer>>

    @FormUrlEncoded
    @POST("index.php?option=com_api&app=users&resource=login&format=raw")
    fun authorize(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<JoomlaAPIResponse<AuthResponse>>

    @POST("index.php?option=com_api&app=mirgar&resource=appeal&format=raw")
    fun sendAppeal(
        @Header("Authorization") authorization: String,
        @Body appeal: Appeal
    ): Call<Int>
}