package com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.models.mainfragmentadapter

import com.sh1p1lov.streamingmusicplayer.domain.models.Genre
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack

data class MainFragmentAdapterData(
    val tracks: List<MusicTrack>,
    val genres: List<Genre>
)