package com.sh1p1lov.streamingmusicplayer.data.api.repository

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.sh1p1lov.streamingmusicplayer.data.api.MusicService
import com.sh1p1lov.streamingmusicplayer.data.api.mappers.mapToMusicTracks
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.repository.MusicServiceRepository


class MusicServiceRepositoryImpl : MusicServiceRepository {

    private val musicService by lazy { MusicService.retrofit.create(MusicService::class.java) }

    override fun getTracks(musicTracksRequestParameters: MusicTracksRequestParameters): List<MusicTrack> {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val results = musicService
            .getTracks()
            .execute().body()?.results ?: listOf()
        return mapToMusicTracks(results)
    }
}