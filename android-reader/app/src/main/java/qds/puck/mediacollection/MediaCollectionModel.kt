package qds.puck.mediacollection

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import qds.puck.api.MediaItem
import qds.puck.api.PuckApi

class MediaCollectionModel : ViewModel() {
    var mediaItems: List<MediaItem> = mutableStateListOf()

    fun updateMediaList(puckApi: PuckApi) = viewModelScope.launch {
        val response = puckApi.getMediaItemList()
        if (response.isSuccessful) {
            mediaItems = response.body()!!
        }
    }
}
