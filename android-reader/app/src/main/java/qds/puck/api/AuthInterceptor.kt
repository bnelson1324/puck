package qds.puck.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val getAccessToken: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String? = getAccessToken()
        val builder = chain.request().newBuilder()
        return if (accessToken != null) {
            chain.proceed(
                builder.header("Authorization", accessToken).build()
            )
        } else {
            chain.proceed(
                builder.build()
            )
        }
    }

}
