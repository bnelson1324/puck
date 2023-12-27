package qds.puck

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import qds.puck.api.*
import qds.puck.data.getMediaCachePath
import qds.puck.data.withoutExtension
import qds.puck.mediadisplay.MediaDisplayModel
import qds.puck.util.*
import java.nio.file.Path
import java.nio.file.Paths

@RunWith(AndroidJUnit4::class)
class MediaDisplayTest {

    @Before
    fun eraseCache() {
        ctx.cacheDir.deleteRecursively()
    }

    private fun copyTestComicToCache() {
        // copy test comic to cache
        for (fileName in testComicFileList.map { it.withoutExtension() }) {
            val assetDirectory: Path = Paths.get("test_comics", fileName)
            val cacheComicPath: Path = getMediaCachePath(ctx, testComicId, fileName)
            copyAssetDirectoryToDirectory(testCtx, assetDirectory, cacheComicPath)
        }

    }

    @Test
    fun mediaDisplayModel_getsCorrectImage() = runBlocking {
        copyTestComicToCache() // simulates downloading the comic
        mediaDisplayModel_downloadsCbz()
    }

    @Test
    fun mediaDisplayModel_downloadsCbz() = runBlocking {
        val mediaDisplayModel = MediaDisplayModel()
        mediaDisplayModel.setCurrentMediaItem(mockPuckApi, ctx, testComic)
        assertEquals(
            getMediaCachePath(ctx, testComicId, "ch1", "p1.png"),
            mediaDisplayModel.currentImagePath!!
        )
    }

    @Test
    fun mediaDisplayModel_hasCorrectPageCount() = runBlocking {
        copyTestComicToCache()
        val mediaDisplayModel = MediaDisplayModel()
        mediaDisplayModel.setCurrentMediaItem(mockPuckApi, ctx, testComic)
        assertEquals(3, mediaDisplayModel.getCurrentCbzSize(ctx))
    }

    @Test
    fun mediaDisplayModel_changesPage() = runBlocking {
        val mediaDisplayModel = MediaDisplayModel()
        mediaDisplayModel.setCurrentMediaItem(mockPuckApi, ctx, testComic)
        mediaDisplayModel.changeCurrentPageIndex(mockPuckApi, ctx, 1).join()
        assertEquals(
            getMediaCachePath(ctx, testComicId, "ch1", "p2.png"),
            mediaDisplayModel.currentImagePath!!
        )
    }
}
