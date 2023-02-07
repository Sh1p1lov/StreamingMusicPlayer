package com.sh1p1lov.streamingmusicplayer.data.api.mappers

import com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack.Result
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack

internal fun mapToMusicTracks(resultList: List<Result>): List<MusicTrack> {
    val musicTracks = mutableListOf<MusicTrack>()
    resultList.forEach {
        musicTracks.add(
            MusicTrack(
                id = it.id,
                name = it.name,
                artist_name = it.artist_name,
                album_image = it.album_image,
                audio_uri = it.audio
            )
        )
    }

    return musicTracks
}