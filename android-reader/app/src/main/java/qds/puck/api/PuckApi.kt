package qds.puck.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import qds.puck.config.serverAddressPort
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
        .baseUrl("https://$serverAddress:$serverAddressPort")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PuckApi::class.java)
}

interface PuckApi {

    @POST("/login")
    suspend fun postLogin(
        @Body password: String
    ): Response<String>

    @GET("/media")
    suspend fun getMediaItemList(): Response<List<MediaItem>>

    @GET("/media/{id}")
    suspend fun getMediaFileList(
        @Path("id") id: Int
    ): Response<List<String>>

    @GET("media/{id}/{file}")
    suspend fun getMediaFile(
        @Path("id") id: Int,
        @Path("file") file: String
    ): Response<ResponseBody>

}
