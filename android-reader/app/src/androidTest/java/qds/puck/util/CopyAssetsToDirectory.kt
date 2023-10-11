package qds.puck.util

import android.content.Context
import java.io.FileOutputStream
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

// copy a folder of assets to a directory
fun copyAssetDirectoryToDirectory(assetContext: Context, assetDirectory: Path, toDirectory: Path) {
    val testComicPagePaths = assetContext.assets.list(assetDirectory.toString())
    for (testComicPageFile: String in testComicPagePaths!!) {
        val assetPath: Path = Paths.get(assetDirectory.toString(), testComicPageFile)
        val toPath: Path = Paths.get(toDirectory.toString(), testComicPageFile)
        copyAssetToDirectory(assetContext, assetPath, toPath)
    }
}

fun copyAssetToDirectory(assetContext: Context, assetPath: Path, toPath: Path) {
    // use asset file's input stream
    assetContext.assets.open(assetPath.toString()).use {
        // write asset file's bytes to copyFile
        toPath.parent!!.createDirectories()
        toPath.createFile()
        val writer = FileOutputStream(toPath.toFile())
        writer.write(it.readBytes())
    }
}
