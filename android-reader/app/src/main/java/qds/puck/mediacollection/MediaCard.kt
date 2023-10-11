import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import qds.puck.api.MediaItem
import qds.puck.api.PuckApi

@Composable
fun MediaCard(
    puckApi: PuckApi,
    mediaItemData: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text("id: ${mediaItemData.id}")
        Text(mediaItemData.name)
    }
}
