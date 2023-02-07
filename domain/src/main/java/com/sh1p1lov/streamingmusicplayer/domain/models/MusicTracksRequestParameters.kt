package com.sh1p1lov.streamingmusicplayer.domain.models

import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.AudioFormat
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.DateBetween
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.ImageSize
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.MusicTracksOrder

data class MusicTracksRequestParameters(
    val order: MusicTracksOrder,
    val limit: Int,
    val audioFormat: AudioFormat,
    val dateBetween: DateBetween,
    val tags: String,
    val imageSize: ImageSize
)
