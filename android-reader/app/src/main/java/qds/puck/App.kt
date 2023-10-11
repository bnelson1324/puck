package qds.puck

import androidx.compose.runtime.Composable
import com.mathroda.snackie.SnackieError
import com.mathroda.snackie.SnackieState
import com.mathroda.snackie.rememberSnackieState
import qds.puck.login.attemptLoginFromPrefs
import qds.puck.nav.AppNavManager

@Composable
fun App() {
    attemptLoginFromPrefs()

    // error snackbar
    val snackieState: SnackieState = rememberSnackieState()
    val onError: (String?) -> Unit = { msg: String? ->
        snackieState.addMessage(msg ?: "")
    }
    SnackieError(
        state = snackieState,
        duration = 5000L
    )

    // app navigation
    AppNavManager(onError)

}
