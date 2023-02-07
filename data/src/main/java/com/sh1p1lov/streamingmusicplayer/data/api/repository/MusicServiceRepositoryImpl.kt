package com.sh1p1lov.streamingmusicplayer.data.api.repository

import com.sh1p1lov.streamingmusicplayer.data.api.MusicService
import com.sh1p1lov.streamingmusicplayer.data.api.mappers.mapToMusicTracks
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.repository.MusicServiceRepository


class MusicServiceRepositoryImpl : MusicServiceRepository {

    private val musicService by lazy { MusicService.retrofit.create(MusicService::class.java) }

    override suspend fun getTracks(musicTracksRequestParameters: MusicTracksRequestParameters): List<MusicTrack> {
        val response = musicService.getTracks(
            musicTracksRequestParameters.order.value,
            musicTracksRequestParameters.limit,
            musicTracksRequestParameters.audioFormat.value,
            musicTracksRequestParameters.dateBetween.value,
            musicTracksRequestParameters.tags,
            musicTracksRequestParameters.imageSize.value
        )
        return mapToMusicTracks(response.body()!!.results)
    }
}