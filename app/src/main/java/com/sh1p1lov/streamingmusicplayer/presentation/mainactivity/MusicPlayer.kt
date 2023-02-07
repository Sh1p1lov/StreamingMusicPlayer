package com.sh1p1lov.streamingmusicplayer.presentation.mainactivity

import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack

interface MusicPlayer {
    fun setMusicPlaylist(playlist: List<MusicTrack>, musicTrackIndex: Int)
    fun setMediaIdUpdatedListener(l: (mediaId: String) -> Unit)
    fun setStoppedListener(l: () -> Unit)
    fun getCurrentMediaId(): String
}