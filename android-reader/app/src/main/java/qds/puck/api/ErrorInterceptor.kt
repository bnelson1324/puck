package qds.puck.api

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorInterceptor(
    private val onError: ((String?) -> Unit)?,
    private val errorMessages: ErrorMessages?,
    private val logout: () -> Unit
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            // handle http errors
            if (response.code >= 400) {
                onError?.invoke("${errorMessages?.error} ${response.code}: ${response.body}")
            }
            return response
        } catch (e: Exception) {
            // handle exceptions
            val msg = when (e) {
                is SocketTimeoutException -> {
                    errorMessages?.connectionTimeout
                }

                is UnknownHostException -> {
                    logout()
                    errorMessages?.badServerAddress
                }

                is ConnectionShutdownException -> {
                    errorMessages?.connectionShutdown
                }

                else -> {
                    e.message ?: errorMessages?.error
                }
            }
            onError?.invoke(msg)

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(e.message ?: errorMessages?.error ?: "")
                .body(msg?.toResponseBody())
                .build()
        }
    }
}

data class ErrorMessages(
    val error: String,
    val connectionTimeout: String,
    val badServerAddress: String,
    val connectionShutdown: String
)
