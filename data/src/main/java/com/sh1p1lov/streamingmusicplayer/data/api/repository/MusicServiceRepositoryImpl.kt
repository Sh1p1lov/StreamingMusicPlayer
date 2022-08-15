package com.sh1p1lov.streamingmusicplayer.data.api.repository

import com.sh1p1lov.streamingmusicplayer.data.api.MusicService
import com.sh1p1lov.streamingmusicplayer.data.api.mappers.mapToMusicTracks
import com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack.MusicTracksList
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.repository.MusicServiceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MusicServiceRepositoryImpl : MusicServiceRepository {

    private val musicService by lazy { MusicService.retrofit.create(MusicService::class.java) }

    override fun getTracks(
        musicTracksRequestParameters: MusicTracksRequestParameters,
        onResponse: (List<MusicTrack>) -> Unit,
        onFailure: (errorMsg: String) -> Unit
    ) {

        musicService.getTracks().enqueue(object : Callback<MusicTracksList> {
            override fun onResponse(
                call: Call<MusicTracksList>,
                response: Response<MusicTracksList>
            ) {
                onResponse.invoke(
                    mapToMusicTracks(
                        response
                            .body()
                            ?.results
                            ?: emptyList())
                )
            }

            override fun onFailure(call: Call<MusicTracksList>, t: Throwable) {
                onFailure.invoke(t.message ?: "")
            }
        })
    }
}