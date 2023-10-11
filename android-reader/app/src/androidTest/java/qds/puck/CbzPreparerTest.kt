package qds.puck

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import qds.puck.data.getMediaCachePath
import qds.puck.data.unzipCbzToCache
import qds.puck.util.copyAssetToDirectory
import qds.puck.util.testCtx
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

@RunWith(AndroidJUnit4::class)
class CbzPreparerTest {

    companion object {
        private val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext

        private const val testComicId: Int = 0
        private const val testCbzFilename = "ch1.cbz"

        @BeforeClass
        @JvmStatic
        fun copyTestCbzToCache() {
            // delete cache for testing
            ctx.cacheDir.deleteRecursively()

            // copy test cbz to cache
            val assetCbzPath: Path = Paths.get("test_comics", testComicId.toString(), testCbzFilename)
            val cacheCbzPath: Path = getMediaCachePath(ctx, testComicId, testCbzFilename)
            copyAssetToDirectory(testCtx, assetCbzPath, cacheCbzPath)
        }
    }

    @Test
    fun cbzPreparer_unzipsCorrectly() {
        val cacheCbzPath: Path = getMediaCachePath(ctx, testComicId, testCbzFilename)
        unzipCbzToCache(ctx, testComicId, cacheCbzPath)

        val cacheComicDirectory: Path = getMediaCachePath(ctx, testComicId, "ch1")
        val cacheComicDirectoryContents: List<String> = cacheComicDirectory.listDirectoryEntries().map { it.name }
        Assert.assertEquals(listOf("p1.png", "p2.png", "p3.png"), cacheComicDirectoryContents)
    }

}
