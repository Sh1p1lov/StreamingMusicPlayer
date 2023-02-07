package com.sh1p1lov.streamingmusicplayer.data.api

import com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack.MusicTracksList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


internal interface MusicService {
    @GET("$API_VERSION/tracks/?client_id=$CLIENT_ID&format=json&include=musicinfo+stats")
    suspend fun getTracks(
        @Query("order") order: String,
        @Query("limit") limit: Int,
        @Query("audioformat") audioFormat: String,
        @Query("datebetween") dateBetween: String,
        @Query("tags") tags: String,
        @Query("imagesize") imageSize: Int
    ): Response<MusicTracksList>

    companion object {
        private const val BASE_URL = "https://api.jamendo.com/"
        private const val CLIENT_ID = "7bd6b959"
        private const val API_VERSION = "v3.0"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}