package com.sh1p1lov.streamingmusicplayer.domain.models

data class MusicTrack(
    val id: String,
    val name: String,
    val artist_name: String,
    val album_image: String,
    val audio_uri: String,
    var isSelected: Boolean = false
)
