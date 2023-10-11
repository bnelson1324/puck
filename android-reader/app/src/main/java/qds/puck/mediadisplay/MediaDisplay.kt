package qds.puck.mediadisplay

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import qds.puck.api.PuckApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

@Composable
fun MediaDisplay(
    puckApi: PuckApi?,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaDisplayModel: MediaDisplayModel = viewModel()
    val ctx = LocalContext.current

    if (puckApi == null) {
        navigateTo("login")
        return
    }

    Column(
        modifier = modifier
    ) {
        if (mediaDisplayModel.currentImagePath != null) {
            val imgPath = mediaDisplayModel.currentImagePath!!.absolutePathString()
            val imgBitmap: Bitmap = BitmapFactory.decodeFile(imgPath)
            Image(
                painter = rememberAsyncImagePainter(imgBitmap),
                contentDescription = mediaDisplayModel.currentImagePath!!.name,
                modifier = Modifier.testTag("imageDisplay")
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.height(24.dp)
        ) {
            Button(
                onClick = { mediaDisplayModel.changeCurrentPageIndex(puckApi, ctx, -1) },
                modifier = Modifier.testTag("prevPageBtn")
            ) {
                Text("Left")
            }
            Text(
                text = "${mediaDisplayModel.currentPageIndex + 1} / ${mediaDisplayModel.getCurrentCbzSize(ctx)}",
                modifier = Modifier.testTag("pageCount")
            )
            Button(
                onClick = { mediaDisplayModel.changeCurrentPageIndex(puckApi, ctx, 1) },
                modifier = Modifier.testTag("nextPageBtn")
            ) {
                Text("Right")
            }
        }
    }
}
