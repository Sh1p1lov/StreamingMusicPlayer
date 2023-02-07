package com.sh1p1lov.streamingmusicplayer.media.data

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.nio.charset.Charset

internal object MediaItemStorage {

    private data class MediaItemData(
        val id: String,
        val title: String,
        val artist: String,
        val artworkUri: String,
        val audioUri: String
    )

    private const val FILE_NAME = "media_items.data"

    fun save(context: Context, mediaItems: List<MediaItem>) {
        val mediaItemsData = mapToMediaItemsData(mediaItems)
        val bytes = serialize(mediaItemsData)
        val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
        fos.write(bytes)
        fos.close()
    }

    fun get(context: Context): List<MediaItem>? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return null
        val fis = context.openFileInput(FILE_NAME)
        if (fis.available() == 0) return null
        val bytes = fis.readBytes()
        fis.close()
        val mediaItemsData = deserialize(bytes) ?: return null
        return mapToMediaItems(mediaItemsData)
    }

    fun clear(context: Context) {
        context.deleteFile(FILE_NAME)
    }

    private fun serialize(mediaItemsData: List<MediaItemData>): ByteArray {
        val gson = Gson()
        val json = gson.toJson(mediaItemsData)
        return json.toByteArray()
    }

    private fun deserialize(bytes: ByteArray): List<MediaItemData>? {
        val json = bytes.toString(Charset.defaultCharset())
        val gson = Gson()
        val items: List<MediaItemData>
        try {
            items = gson.fromJson(json, Array<MediaItemData>::class.java).toList()
        } catch (e: JsonSyntaxException) {
            return null
        }
        return items
    }

    private fun mapToMediaItemsData(mediaItems: List<MediaItem>): List<MediaItemData> {
        val mediaItemsData = mutableListOf<MediaItemData>()
        mediaItems.forEach {
            mediaItemsData.add(mapToMediaItemData(it))
        }
        return mediaItemsData
    }

    private fun mapToMediaItems(mediaItemsData: List<MediaItemData>): List<MediaItem> {
        val mediaItems = mutableListOf<MediaItem>()
        mediaItemsData.forEach {
            mediaItems.add(mapToMediaItem(it))
        }
        return mediaItems
    }

    private fun mapToMediaItemData(mediaItem: MediaItem): MediaItemData {
        return MediaItemData(
            id = mediaItem.mediaId,
            title = mediaItem.mediaMetadata.title.toString(),
            artist = mediaItem.mediaMetadata.artist.toString(),
            artworkUri = mediaItem.mediaMetadata.artworkUri.toString(),
            audioUri = mediaItem.localConfiguration?.uri.toString()
        )
    }

    private fun mapToMediaItem(mediaItemData: MediaItemData): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(mediaItemData.title)
            .setArtist(mediaItemData.artist)
            .setArtworkUri(Uri.parse(mediaItemData.artworkUri))
            .build()
        return MediaItem.Builder()
            .setMediaId(mediaItemData.id)
            .setUri(mediaItemData.audioUri)
            .setMediaMetadata(mediaMetadata)
            .build()
    }
}