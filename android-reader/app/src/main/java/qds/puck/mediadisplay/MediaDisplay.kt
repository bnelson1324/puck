package qds.puck.mediadisplay

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import qds.puck.api.PuckApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

@Composable
fun MediaDisplay(
    puckApi: PuckApi?,
    navigateTo: (String) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
) {
    val mediaDisplayModel: MediaDisplayModel = viewModel(viewModelStoreOwner)
    val ctx = LocalContext.current

    if (puckApi == null) {
        navigateTo("login")
        return
    }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        if (mediaDisplayModel.currentImagePath != null) {
            val imgPath = mediaDisplayModel.currentImagePath!!.absolutePathString()
            val imgBitmap: Bitmap = BitmapFactory.decodeFile(imgPath)
            Image(
                painter = rememberAsyncImagePainter(imgBitmap),
                contentDescription = mediaDisplayModel.currentImagePath!!.name,
                modifier = Modifier.fillMaxWidth().border(BorderStroke(2.dp, Color.Black)).testTag("imageDisplay")
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp).fillMaxWidth().height(36.dp)
        ) {
            Button(
                onClick = { mediaDisplayModel.changeCurrentPageIndex(puckApi, ctx, -1) },
                modifier = Modifier.testTag("prevPageBtn")
            ) {
                Text("Left")
            }
            Text(
                text = "${mediaDisplayModel.currentPageIndex + 1} / ${mediaDisplayModel.getCurrentCbzSize(ctx)}",
                modifier = Modifier.padding(12.dp, 0.dp).testTag("pageCount")
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
