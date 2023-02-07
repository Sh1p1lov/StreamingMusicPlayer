package com.sh1p1lov.streamingmusicplayer.presentation.mainactivity

import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack

fun convertToMediaItems(playlist: List<MusicTrack>): List<MediaItem> {
    val mediaItems = mutableListOf<MediaItem>()
    playlist.forEach {
        mediaItems.add(convertToMediaItem(it))
    }
    return mediaItems
}

private fun convertToMediaItem(musicTrack: MusicTrack): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(musicTrack.name)
        .setArtist(musicTrack.artist_name)
        .setArtworkUri(Uri.parse(musicTrack.album_image))
        .build()
    val mediaItem = MediaItem.Builder()
        .setMediaId(musicTrack.id)
        .setUri(musicTrack.audio_uri)
        .setMediaMetadata(metadata)
        .build()
    return mediaItem
}