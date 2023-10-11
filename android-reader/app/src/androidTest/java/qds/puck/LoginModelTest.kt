package qds.puck

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import qds.puck.config.prefAccessTokenKey
import qds.puck.config.prefServerAddressKey
import qds.puck.login.LoginModel
import qds.puck.util.ctx

@RunWith(AndroidJUnit4::class)
class LoginModelTest {

    @Before
    fun clearSessionPrefs() {
        with(getSessionPrefs(ctx).edit()) {
            clear()
            commit()
        }
    }

    @Test
    fun loginModel_accessTokenNullByDefault() {
        assertEquals(null, getAccessToken())
    }

    @Test
    fun loginModel_loginsInFromPrefs() {
        // write some values to sharedPrefs
        val testServerAddress = "localhost"
        val testAccessToken = "abcd1234"
        with(getSessionPrefs(ctx).edit()) {
            putString(prefServerAddressKey, testServerAddress)
            putString(prefAccessTokenKey, testAccessToken)
            commit()
        }

        // have model log in from preferences
        val loginModel = LoginModel()
        loginModel.setPuckApiFromPrefs(ctx)

        assert(loginModel.isLoggingIn)
    }

    private fun getAccessToken(): String? {
        return getSessionPrefs(ctx).getString(prefAccessTokenKey, null)
    }

    private fun getSessionPrefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences("session", Context.MODE_PRIVATE)

}
