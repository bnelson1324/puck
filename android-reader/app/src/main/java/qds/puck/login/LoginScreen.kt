package qds.puck.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import qds.puck.R
import qds.puck.api.ErrorMessages
import qds.puck.config.serverAddressPort

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onError: (String?) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier
) {

    val loginModel: LoginModel = viewModel(viewModelStoreOwner)
    val ctx = LocalContext.current
    loginModel.onError = onError
    loginModel.errorMessages = getErrorMessages()

    var serverAddress: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }

    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!loginModel.isLoggingIn) {
            Text(text = "Login", modifier = Modifier.padding(6.dp))
            TextField(
                value = serverAddress,
                onValueChange = { serverAddress = it.trim() },
                label = { Text("Server address") },
                trailingIcon = {
                    Text(
                        text = ":$serverAddressPort",
                        modifier = Modifier.alpha(0.6f)
                    )
                }
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    loginModel.login(ctx, serverAddress, password)
                    serverAddress = ""
                    password = ""
                    keyboardController?.hide()
                },
                modifier = Modifier.padding(6.dp)
            ) {
                Text("Log In")
            }
        } else {
            val displayServerAddress: String? = loginModel.displayServerAddress
            if (displayServerAddress == null) {
                Text("Logging in...")
            } else {
                Text(text = "Currently logged in", modifier = Modifier.padding(2.dp))
                Text(text = "Server Address: $displayServerAddress")
            }

            Button(
                onClick = { loginModel.logout(ctx) },
                modifier = Modifier.padding(6.dp)
            ) {
                Text("Log Out")
            }
        }
    }

}

@Composable
private fun getErrorMessages() = ErrorMessages(
    error = stringResource(R.string.error),
    badServerAddress = stringResource(R.string.err_bad_server_address),
    connectionTimeout = stringResource(R.string.err_connection_timed_out),
    connectionShutdown = stringResource(R.string.err_connection_shutdown)
)
