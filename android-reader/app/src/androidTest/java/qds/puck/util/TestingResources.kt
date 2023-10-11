package qds.puck.util

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import qds.puck.api.MediaItem
import qds.puck.api.MockPuckApi

val mockPuckApi = MockPuckApi()
val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
val testCtx: Context = InstrumentationRegistry.getInstrumentation().context

val testMediaCollection: List<MediaItem> = listOf(
    MediaItem(0, "Some Comic"),
    MediaItem(1, "Another Comic")
)

const val testComicId: Int = 0
val testComicFileList: List<String> = (1..3).toList().map { "ch$it.cbz" }

const val testPassword: String = "pw123"
const val testAuthToken: String = "abcd1234"
