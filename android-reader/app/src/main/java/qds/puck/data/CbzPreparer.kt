package qds.puck.data

import android.content.Context
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipInputStream
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.nameWithoutExtension

fun unzipCbzToCache(ctx: Context, mediaId: Int, cbzPath: Path) {
    // determine location in cache to store the unzipped file
    val unzippedDataDirectory = getMediaCachePath(ctx, mediaId, cbzPath.nameWithoutExtension)

    // unzip the file
    ZipInputStream(FileInputStream(cbzPath.toFile())).use {
        val buffer = ByteArray(16384)
        while (true) {
            val zipEntry = it.nextEntry ?: break
            val newFilePath: Path = Paths.get(unzippedDataDirectory.toString(), zipEntry.name)

            if (zipEntry.isDirectory) {
                newFilePath.createDirectories()
            } else {
                // fix for Windows-created archives
                val parent: Path = newFilePath.parent
                if (!parent.isDirectory()) {
                    parent.createDirectories()
                }

                // write file content
                FileOutputStream(newFilePath.toFile()).use { fos ->
                    var len: Int
                    while (it.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                }
            }
        }
    }
}
