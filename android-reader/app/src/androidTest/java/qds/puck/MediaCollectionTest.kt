package qds.puck

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import qds.puck.mediacollection.MediaCollectionModel
import qds.puck.util.mockPuckApi
import qds.puck.util.testMediaCollection

@RunWith(AndroidJUnit4::class)
class MediaCollectionTest {

    @Test
    fun mediaCollectionModel_updatesMediaList() = runBlocking {
        val mediaCollectionModel = MediaCollectionModel()
        mediaCollectionModel.updateMediaList(mockPuckApi).join()
        Assert.assertArrayEquals(testMediaCollection.toTypedArray(), mediaCollectionModel.mediaItems.toTypedArray())
    }

}
