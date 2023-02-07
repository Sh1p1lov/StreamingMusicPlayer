package com.sh1p1lov.streamingmusicplayer.domain.repository

import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters

interface MusicServiceRepository {
    suspend fun getTracks(musicTracksRequestParameters: MusicTracksRequestParameters): List<MusicTrack>
}