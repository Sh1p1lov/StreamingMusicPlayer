package com.sh1p1lov.streamingmusicplayer.domain.models

import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.MusicTracksOrder

data class MusicTracksRequestParameters(
    val order: MusicTracksOrder,
    val limit: Int
)
