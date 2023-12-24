package qds.puck.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import qds.puck.config.serverAddressPort
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

fun createApi(
    serverAddress: String,
    getAccessToken: () -> String?,
    onError: ((String?) -> Unit)?,
    errorMessages: ErrorMessages?,
    logout: () -> Unit
): PuckApi? {
    if (serverAddress == "") {
        onError?.invoke(errorMessages?.badServerAddress)
        return null
    }

    val httpClientBuilder = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(getAccessToken))
        .addInterceptor(ErrorInterceptor(onError, errorMessages, logout))

    return Retrofit.Builder().client(httpClientBuilder.build())
        .baseUrl("http://$serverAddress:$serverAddressPort")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PuckApi::class.java)
}

interface PuckApi {
    @FormUrlEncoded
    @POST("/login")
    suspend fun postLogin(
        @Field("password") password: String
    ): Response<String>

    @DELETE("/login")
    suspend fun logout(
    ): Response<ResponseBody>

    @GET("/media")
    suspend fun getMediaItemList(): Response<List<MediaItem>>

    @GET("media/{id}")
    suspend fun getMediaFile(
        @Path("id") id: Int,
    ): Response<ResponseBody>
}
