package qds.puck.data

import android.content.Context
import java.nio.file.Path
import java.nio.file.Paths

fun getMediaCachePath(
    ctx: Context,
    mediaId: Int,
    vararg subPath: String
): Path {
    return Paths.get(ctx.cacheDir.toString(), "media", mediaId.toString(), *subPath)
}

fun String.withoutExtension(): String {
    return this.substring(0, this.indexOf('.'))
}
