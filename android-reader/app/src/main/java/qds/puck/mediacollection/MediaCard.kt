import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary))
            .clickable { onClick() }
    ) {
        Text(
            text = mediaItemData.name,
            modifier = modifier.padding(6.dp)
        )
        Text(
            text = "?",
            fontSize = 128.sp,
            modifier = modifier.padding(6.dp)
        )
    }
}
