package com.sh1p1lov.streamingmusicplayer.data.api.mappers

import com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack.Result
import com.sh1p1lov.streamingmusicplayer.data.api.repository.MusicServiceRepositoryImpl
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack

internal fun MusicServiceRepositoryImpl.mapToMusicTracks(resultList: List<Result>): List<MusicTrack> {
    val musicTracks = mutableListOf<MusicTrack>()
    resultList.forEach { it ->
        musicTracks.add(
            MusicTrack(
                name = it.name,
                artist_name = it.artist_name,
                album_image = it.album_image
            )
        )
    }

    return musicTracks
}