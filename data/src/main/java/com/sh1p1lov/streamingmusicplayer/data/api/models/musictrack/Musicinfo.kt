package com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack

internal data class Musicinfo(
    val acousticelectric: String,
    val gender: String,
    val lang: String,
    val speed: String,
    val tags: Tags,
    val vocalinstrumental: String
)