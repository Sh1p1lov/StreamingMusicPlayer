package com.sh1p1lov.streamingmusicplayer.data.api

import com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack.MusicTracksList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicServiceNewAfterCommit {

    @GET("${MusicService.API_VERSION}/tracks/?client_id=${MusicService.CLIENT_ID}&format=json&include=musicinfo+stats")
    suspend fun getTracks(
        @Query("order") order: String,
        @Query("limit") limit: Int,
        @Query("audioformat") audioFormat: String,
        @Query("datebetween") dateBetween: String,
        @Query("tags") tags: String,
        @Query("imagesize") imageSize: Int
    ): Response<MusicTracksList>
}