package com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack

internal data class Stats(
    val avgnote: Double,
    val dislikes: Int,
    val favorited: Int,
    val likes: Int,
    val notes: Int,
    val playlisted: Int,
    val rate_downloads_total: Int,
    val rate_listened_total: Int
)