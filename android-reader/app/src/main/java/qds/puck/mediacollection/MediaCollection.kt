package qds.puck.mediacollection

import MediaCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import qds.puck.api.PuckApi
import qds.puck.mediadisplay.MediaDisplayModel

@Composable
fun MediaCollection(puckApi: PuckApi?, navigateTo: (String) -> Unit, modifier: Modifier = Modifier) {
    val mediaCollectionModel: MediaCollectionModel = viewModel()

    if (puckApi == null) {
        navigateTo("login")
        return
    }

    Column(modifier = modifier) {
        Button(onClick = { mediaCollectionModel.updateMediaList(puckApi) }) {
            Text("Refresh")
        }

        // display media cards
        val mediaDisplayModel: MediaDisplayModel = viewModel()
        val ctx = LocalContext.current
        LazyVerticalGrid(GridCells.Fixed(2)) {
            items(mediaCollectionModel.mediaItems) { mediaItem ->
                val onClick: () -> Unit = {
                    mediaDisplayModel.setCurrentMediaItem(puckApi, ctx, mediaItem.id)
                    navigateTo("reader")
                }
                MediaCard(puckApi, mediaItem, onClick)
            }
        }
    }
}
