package com.sh1p1lov.streamingmusicplayer.presentation.mainfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.MusicTracksOrder
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.usecase.GetTracksUseCase

class MainFragmentViewModel(
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {

    private val mutableMusicTracksList = MutableLiveData<List<MusicTrack>>()
    val musicTracksList: LiveData<List<MusicTrack>> = mutableMusicTracksList

    companion object {
        private const val LIMIT = 10
    }

    fun getTop10TracksOfMonth() {
        val musicTracksRequestParameters =
            MusicTracksRequestParameters(
                MusicTracksOrder.POPULARITY_MONTH,
                LIMIT
            )
        getTracksUseCase
            .execute(
                musicTracksRequestParameters,
                { musicTracks ->
                    mutableMusicTracksList.value = musicTracks
                },
                { errorMsg ->
                    Log.e("ERROR_MSG", errorMsg)
                }
            )
    }
}