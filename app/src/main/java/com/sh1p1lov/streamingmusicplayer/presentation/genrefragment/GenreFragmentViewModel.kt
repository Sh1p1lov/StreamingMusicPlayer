package com.sh1p1lov.streamingmusicplayer.presentation.genrefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.AudioFormat
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.DateBetween
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.ImageSize
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.MusicTracksOrder
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.usecase.GetTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenreFragmentViewModel(
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {

    private val mutableTracks = MutableLiveData<List<MusicTrack>>()
    val track: LiveData<List<MusicTrack>> = mutableTracks

    companion object {
        private const val TAG = "GenreFragmentLog"
        private const val TRACKS_LIMIT = 200
    }

    fun getTracksByTag(tag: String) {
        viewModelScope.launch {
            val musicTracksRequestParameters =
                MusicTracksRequestParameters(
                    MusicTracksOrder.RELEVANCE,
                    TRACKS_LIMIT,
                    AudioFormat.MP32,
                    DateBetween(),
                    tag,
                    ImageSize.SIZE_100
                )
            val list = withContext(Dispatchers.IO) { getTracksUseCase.execute(musicTracksRequestParameters) }
            mutableTracks.value = list
        }
    }
}