package qds.puck.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import qds.puck.login.LoginModel
import qds.puck.login.LoginScreen
import qds.puck.mediacollection.MediaCollection
import qds.puck.mediadisplay.MediaDisplay
import qds.puck.mediadisplay.MediaDisplayModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavManager(onError: (String?) -> Unit, modifier: Modifier = Modifier) {
    var currentItemIndex: Int by remember { mutableStateOf(2) }

    val navController = rememberNavController()
    val navigateTo: (String) -> Unit = { destinationRoute: String ->
        navController.navigate(destinationRoute)
        currentItemIndex = navRoutes.indexOfFirst { it.route == destinationRoute }
    }

    val loginModel: LoginModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                navRoutes.forEachIndexed { index, navRoute ->
                    NavigationBarItem(
                        icon = { Icon(navRoute.icon, navRoute.route) },
                        label = { Text(stringResource(navRoute.labelId)) },
                        selected = index == currentItemIndex,
                        enabled = navRoute.enabledFromIsLoggedIn(loginModel.isLoggedIn),
                        onClick = { navigateTo(navRoute.route) }
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val viewModelStoreOwner = LocalViewModelStoreOwner.current!!
        NavHost(
            navController = navController,
            startDestination = "login",
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable("mediaCollection") {
                MediaCollection(loginModel.puckApi, navigateTo, viewModelStoreOwner)
            }
            composable("reader") {
                val mediaDisplayModel: MediaDisplayModel = viewModel(viewModelStoreOwner)
                if (mediaDisplayModel.currentMedia != null) {
                    MediaDisplay(loginModel.puckApi, navigateTo, viewModelStoreOwner)
                } else {
                    navigateTo("mediaCollection")
                }
            }
            composable("login") {
                LoginScreen(onError, viewModelStoreOwner)
            }
        }
    }
}
