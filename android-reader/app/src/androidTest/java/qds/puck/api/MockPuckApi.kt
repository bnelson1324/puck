package qds.puck.api

import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import qds.puck.util.*
import retrofit2.Response
import java.nio.file.Paths

class MockPuckApi : PuckApi {

    override suspend fun postLogin(password: String): Response<String> {
        return if (password == testPassword) {
            Response.success(testAuthToken)
        } else {
            Response.error(403, "Incorrect password".toResponseBody())
        }
    }

    override suspend fun logout(): Response<Void> {
        return Response.success(null)
    }

    override suspend fun getMediaItemList(): Response<List<MediaItem>> {
        return Response.success(testMediaCollection)
    }

    override suspend fun getMediaFile(id: Int): Response<ResponseBody> {
        val fileName = testMediaCollection.find { mediaItem -> mediaItem.id == id }!!.fileName
        val assetPath = Paths.get("test_comics", fileName)
        testCtx.assets.open(assetPath.toString()).use {
            val responseBody = it.readBytes().toResponseBody()
            return Response.success(responseBody)
        }
    }

}
