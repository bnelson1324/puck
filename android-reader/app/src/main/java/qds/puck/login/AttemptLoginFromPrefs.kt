package qds.puck.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun attemptLoginFromPrefs() {
    val ctx = LocalContext.current
    val loginModel: LoginModel = viewModel()
    LaunchedEffect(true) {
        if (!loginModel.isLoggingIn) {
            loginModel.setPuckApiFromPrefs(ctx)
        }
    }
}
