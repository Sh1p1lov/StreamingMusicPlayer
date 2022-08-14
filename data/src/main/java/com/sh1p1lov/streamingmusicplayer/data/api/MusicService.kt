package com.sh1p1lov.streamingmusicplayer.data.api

import com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack.MusicTracksList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


internal interface MusicService {
//    @GET("v3.0/tracks/?client_id=7bd6b959&format=json&audioformat=mp32&order={order}&limit={limit}&include=musicinfo+stats")
//    fun getTracks(@Path("order") order: String, @Path("limit") limit: Int): Call<MusicTracksList>

    @GET("v3.0/tracks/?client_id=7bd6b959&format=json&audioformat=mp32&order=popularity_month&limit=10&include=musicinfo+stats")
    fun getTracks(): Call<MusicTracksList>

//    @GET("$API_VERSION/tracks/?client_id=$CLIENT_ID&format=json&limit=10&audioformat=mp32&include=musicinfo+stats&datebetween=2021-08-01_2022-12-31&order=listens_total")
//    fun getTracks(): Call<MusicTracksList>

    companion object {
        private const val BASE_URL = "https://api.jamendo.com/"
        private const val CLIENT_ID = "7bd6b959"
        private const val API_VERSION = "v3.0"
        val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
}