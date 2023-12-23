package qds.puck.mediadisplay

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import qds.puck.api.MediaItem
import qds.puck.api.PuckApi
import qds.puck.data.getMediaCachePath
import qds.puck.data.unzipCbzToCache
import qds.puck.data.withoutExtension
import java.io.FileOutputStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries

class MediaDisplayModel : ViewModel() {

    /* state */
    // the id of the comic
    private var currentMedia: MediaItem? = null

    var currentPageIndex: Int = 0
        private set

    var currentImagePath: Path? by mutableStateOf(null)
        private set

    private fun getCurrentMediaDirectory(ctx: Context): Path =
        getMediaCachePath(ctx, currentMedia!!.id, currentMedia!!.fileName.withoutExtension())

    private fun getCurrentCbzFilePath(ctx: Context): Path =
        getMediaCachePath(ctx, currentMedia!!.id, currentMedia!!.fileName)

    fun getCurrentCbzSize(ctx: Context): Int = getCurrentMediaDirectory(ctx).listDirectoryEntries().size


    /* functions */
    suspend fun setCurrentMediaItem(puckApi: PuckApi, ctx: Context, mediaItem: MediaItem) {
        currentMedia = mediaItem
        fetchCurrentDirectoryIfNotCached(puckApi, ctx)

        currentPageIndex = 0
        loadCurrentImage(ctx)
    }

    fun changeCurrentPageIndex(puckApi: PuckApi, ctx: Context, pageAmount: Int) = viewModelScope.launch {
        // change currentPageIndex and currentCbzIndex
        if (currentImagePath != null) {
            currentPageIndex += pageAmount

            // load current image
            loadCurrentImage(ctx)
        }
    }

    private suspend fun fetchCurrentDirectoryIfNotCached(puckApi: PuckApi, ctx: Context) {
        // checks if the directory exists. if it doesn't, make a network call to fetch the appropriate cbz and unzip it
        if (!getCurrentMediaDirectory(ctx).exists()) {
            puckApi.getMediaFile(currentMedia!!.id).body()!!.byteStream().use { bs ->
                val writeFile = getCurrentCbzFilePath(ctx)
                writeFile.parent.createDirectories()
                writeFile.createFile()
                FileOutputStream(writeFile.toFile()).use { fos ->
                    val buffer = ByteArray(16384)
                    var len: Int
                    while (bs.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                }
            }
            unzipCbzToCache(ctx, currentMedia!!.id, getCurrentCbzFilePath(ctx))
        }
    }

    private fun loadCurrentImage(ctx: Context) {
        val currentDirectoryImgPaths: List<Path> = getCurrentMediaDirectory(ctx).listDirectoryEntries()
        val imgPath: Path = currentDirectoryImgPaths[currentPageIndex]
        currentImagePath = imgPath
    }

}
