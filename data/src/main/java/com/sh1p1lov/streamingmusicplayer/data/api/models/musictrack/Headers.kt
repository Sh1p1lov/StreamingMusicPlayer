package com.sh1p1lov.streamingmusicplayer.data.api.models.musictrack

internal data class Headers(
    val code: Int,
    val error_message: String,
    val next: String,
    val results_count: Int,
    val status: String,
    val warnings: String
)