package qds.puck.login

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import qds.puck.api.ErrorMessages
import qds.puck.api.PuckApi
import qds.puck.api.createApi
import qds.puck.config.prefAccessTokenKey
import qds.puck.config.prefServerAddressKey

class LoginModel : ViewModel() {

    var puckApi: PuckApi? by mutableStateOf(null)
        private set
    val isLoggingIn: Boolean
        get() = puckApi != null
    val isLoggedIn: Boolean
        get() = puckApi != null && displayServerAddress != null

    var displayServerAddress: String? by mutableStateOf(null)

    var onError: ((String?) -> Unit)? = null
    var errorMessages: ErrorMessages? = null

    /* managing login */
    fun login(ctx: Context, serverAddress: String, password: String) = viewModelScope.launch {
        setPuckApi(serverAddress, { getAccessToken(ctx) }, onError) { logout(ctx) }
        if (puckApi == null) {
            return@launch
        }

        val response = puckApi!!.postLogin(password)
        if (response.isSuccessful) {
            displayServerAddress = serverAddress

            // save serverAddress and token to preferences
            val accessToken: String = response.body()!!
            with(getSessionPrefs(ctx).edit()) {
                putString(prefServerAddressKey, serverAddress)
                putString(prefAccessTokenKey, accessToken)
                commit()
            }
        }
    }

    fun setPuckApiFromPrefs(ctx: Context) {
        val serverAddress: String? = getServerAddress(ctx)
        val accessToken: String? = getAccessToken(ctx)

        if (serverAddress != null && accessToken != null) {
            setPuckApi(serverAddress, { getAccessToken(ctx) }, onError) { logout(ctx) }
            displayServerAddress = serverAddress
        }
    }

    fun logout(ctx: Context) {
        puckApi = null
        displayServerAddress = null

        // delete server address and token from preferences
        with(getSessionPrefs(ctx).edit()) {
            remove(prefServerAddressKey)
            remove(prefAccessTokenKey)
            commit()
        }

        // TODO: call logout endpoint - tell server to log out & forget token
    }

    private fun setPuckApi(
        serverAddress: String,
        getAccessToken: () -> String?,
        onError: ((String?) -> Unit)?,
        logout: () -> Unit
    ) {
        puckApi = createApi(serverAddress, getAccessToken, onError, errorMessages, logout)
        if (puckApi == null) {
            logout()
        }
    }

    /* preferences */
    private fun getServerAddress(ctx: Context) = getSessionPrefs(ctx).getString(prefServerAddressKey, null)

    private fun getAccessToken(ctx: Context) = getSessionPrefs(ctx).getString(prefAccessTokenKey, null)

    private fun getSessionPrefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences("session", Context.MODE_PRIVATE)

}
