package qds.puck.mediacollection

import MediaCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import qds.puck.api.PuckApi
import qds.puck.mediadisplay.MediaDisplayModel

@Composable
fun MediaCollection(
    puckApi: PuckApi?,
    navigateTo: (String) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier
) {
    val mediaCollectionModel: MediaCollectionModel = viewModel(viewModelStoreOwner)

    if (puckApi == null) {
        navigateTo("login")
        return
    }

    val ctx = LocalContext.current

    LaunchedEffect(true) {
        mediaCollectionModel.updateMediaList(puckApi)
    }

    Column(
        modifier = modifier
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { ctx.cacheDir.deleteRecursively() },
                modifier = modifier.padding(12.dp, 6.dp)
            ) {
                Text("Clear Cache")
            }

            Button(
                onClick = { mediaCollectionModel.updateMediaList(puckApi) },
                modifier = modifier.padding(12.dp, 6.dp)
            ) {
                Text("Refresh")
            }
        }

        // display media cards
        val mediaDisplayModel: MediaDisplayModel = viewModel(viewModelStoreOwner)
        val ctx = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        LazyVerticalGrid(GridCells.Fixed(2)) {
            items(
                items = mediaCollectionModel.mediaItems,
                key = { mediaItem -> mediaItem.id }
            ) { mediaItem ->
                val onClick: () -> Unit = {
                    coroutineScope.launch {
                        mediaDisplayModel.setCurrentMediaItem(puckApi, ctx, mediaItem)
                        navigateTo("reader")
                    }
                }
                MediaCard(puckApi, mediaItem, onClick)
            }
        }
    }
}
