package qds.puck.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import qds.puck.R

val navRoutes: List<NavRoute> = listOf(
    NavRoute(
        "mediaCollection",
        Icons.Filled.Home,
        R.string.nav_media_collection
    ) { it },
    NavRoute(
        "reader",
        Icons.Filled.Search,
        R.string.nav_reader
    ) { it },
    NavRoute(
        "login",
        Icons.Filled.Settings,
        R.string.nav_login
    ) { true }
)

data class NavRoute(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelId: Int,
    val enabledFromIsLoggedIn: (Boolean) -> Boolean
)
