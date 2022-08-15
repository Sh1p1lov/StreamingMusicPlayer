package com.sh1p1lov.streamingmusicplayer.domain.repository

import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters

interface MusicServiceRepository {
    fun getTracks(
        musicTracksRequestParameters: MusicTracksRequestParameters,
        onResponse: (List<MusicTrack>) -> Unit,
        onFailure: (errorMsg: String) -> Unit
    )
}