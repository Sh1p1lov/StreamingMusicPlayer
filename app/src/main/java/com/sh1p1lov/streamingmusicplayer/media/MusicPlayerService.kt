package com.sh1p1lov.streamingmusicplayer.media

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.RatingCompat.RATING_NONE
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.sh1p1lov.streamingmusicplayer.media.data.MediaItemStorage
import com.sh1p1lov.streamingmusicplayer.notification.MediaStyleNotification
import com.sh1p1lov.streamingmusicplayer.receiver.NotificationDeleteReceiver
import com.squareup.picasso.Picasso
import java.lang.Exception

class MusicPlayerService : Service() {

    companion object {
        private const val TAG = "MusicPlayerService"
        private const val LAST_MEDIA_ID_DEFAULT = ""
        private const val PREF_NAME = "MusicPlayerServicePreferences"
        private const val PREF_KEY_CURRENT_MEDIA_ITEM_INDEX = "CURRENT_MEDIA_ITEM_INDEX"
        private const val PREF_KEY_CURRENT_POSITION = "CURRENT_POSITION"
        private const val PREF_KEY_FLAG_BOUND = "FLAG_BOUND"
    }

    private val binder: MusicPlayerServiceBinder = MusicPlayerServiceBinder()
    private var mediaSession: MediaSessionCompat? = null
    private var transportControls: MediaControllerCompat.TransportControls? = null
    private var player: ExoPlayer? = null
    private var notificationLargeIconBitmap: Bitmap? = null
    private var lastMediaId = LAST_MEDIA_ID_DEFAULT
    private var isMediaItemsRestored = false
    private var isFirstSet = false
    private var mediaMetadataChangedListener: ((mediaMetadata: MediaMetadata) -> Unit)? = null
    private var notificationDeletedListener: (() -> Unit)? = null
    private val preferences by lazy { baseContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }
    private val playbackStateBuilder = PlaybackStateCompat.Builder()
        .setActions(
            PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                    or PlaybackStateCompat.ACTION_PLAY_FROM_URI
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PREPARE
                    or PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH
                    or PlaybackStateCompat.ACTION_PREPARE_FROM_URI
                    or PlaybackStateCompat.ACTION_REWIND
                    or PlaybackStateCompat.ACTION_FAST_FORWARD
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    or PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM
                    or PlaybackStateCompat.ACTION_SEEK_TO
                    or PlaybackStateCompat.ACTION_SET_REPEAT_MODE
                    or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
        )

    inner class MusicPlayerServiceBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    fun getPlayer(): ExoPlayer? = player

    fun setMediaItems(mediaItems: List<MediaItem>, playMediaItemIndex: Int) {
        if (playMediaItemIndex != 0) isFirstSet = true
        lastMediaId = LAST_MEDIA_ID_DEFAULT
        isMediaItemsRestored = true
        preferences
            .edit()
            .remove(PREF_KEY_CURRENT_MEDIA_ITEM_INDEX)
            .remove(PREF_KEY_CURRENT_POSITION)
            .apply()
        MediaItemStorage.save(baseContext, mediaItems)
        player?.setMediaItems(mediaItems)
        transportControls?.skipToQueueItem(playMediaItemIndex.toLong())
        transportControls?.play()
    }

    fun setMediaMetadataChangedListener(l: ((mediaMetadata: MediaMetadata) -> Unit)?) {
        mediaMetadataChangedListener = l
    }

    fun setNotificationDeletedListener(l: (() -> Unit)?) {
        notificationDeletedListener = l
    }

    fun getCurrentMediaMetadata(): MediaMetadata? {
        val m = player?.mediaMetadata
        return m?.let { if (it == MediaMetadata.EMPTY) null else it }
    }

    fun getCurrentMediaId(): String = player?.currentMediaItem?.mediaId ?: ""

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()

        initPlayer()
        initMediaSession()
        initTransportController()

        mediaSession?.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                Log.d(TAG, "onPlay")
                player?.let { p ->
                    p.playWhenReady = true
                    if (p.playbackState != Player.STATE_READY)
                        transportControls?.prepare()
                }
            }

            override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            }

            override fun onPlayFromSearch(query: String?, extras: Bundle?) {
            }

            override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            }

            override fun onPause() {
                Log.d(TAG, "onPause")
                player?.pause()
            }

            override fun onStop() {
                Log.d(TAG, "onStop")
                player?.pause()
            }

            override fun onPrepare() {
                Log.d(TAG, "onPrepare")
                player?.prepare()
            }

            override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
            }

            override fun onPrepareFromSearch(query: String?, extras: Bundle?) {
            }

            override fun onPrepareFromUri(uri: Uri?, extras: Bundle?) {
            }

            override fun onRewind() {
                Log.d(TAG, "onRewind")
                player?.seekBack()
            }

            override fun onFastForward() {
                Log.d(TAG, "onFastForward")
                player?.seekForward()
            }

            override fun onSkipToPrevious() {
                Log.d(TAG, "onSkipToPrevious")
                player?.seekToPreviousMediaItem()
            }

            override fun onSkipToNext() {
                Log.d(TAG, "onSkipToNext")
                player?.seekToNextMediaItem()
            }

            override fun onSkipToQueueItem(id: Long) {
                Log.d(TAG, "onSkipToQueueItem")
                player?.seekTo(id.toInt(), 0)
            }

            override fun onSeekTo(pos: Long) {
                Log.d(TAG, "onSeekTo")
                player?.seekTo(pos)
            }

            override fun onSetRepeatMode(repeatMode: Int) {
            }

            override fun onSetShuffleMode(shuffleMode: Int) {
            }

            override fun onSetCaptioningEnabled(enabled: Boolean) {
            }
        })

        player?.let { p ->
            p.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_IDLE -> {
                            Log.d(TAG, "Player.STATE_IDLE")
                            setMediaPlaybackState(PlaybackStateCompat.STATE_STOPPED, 0f)
                        }
                        Player.STATE_BUFFERING -> {
                            Log.d(TAG, "Player.STATE_BUFFERING")
                            setMediaPlaybackState(PlaybackStateCompat.STATE_BUFFERING, 0f)
                            updateSessionMetadata()
                        }
                        Player.STATE_READY -> {
                            Log.d(TAG, "Player.STATE_READY")
                            setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED, 0f)
                            updateSessionMetadata()
                        }
                        Player.STATE_ENDED -> { Log.d(TAG, "Player.STATE_ENDED") }
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        Log.d(TAG, "isPlaying: true")
                        notificationLargeIconBitmap?.let { artwork ->
                            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING, p.playbackParameters.speed)
                            val notification = createMediaNotification(p.mediaMetadata, artwork, true)
                            startForeground(MediaStyleNotification.NOTIFICATION_ID, notification)
                        }
                    }
                    else {
                        Log.d(TAG, "isPlaying: false")
                        notificationLargeIconBitmap?.let { artwork ->
                            saveCurrentPosition(p.currentPosition)
                            setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED, 0f)
                            val notification = createMediaNotification(p.mediaMetadata, artwork, false)
                            showNotification(notification)
                            stopForeground(STOP_FOREGROUND_DETACH)
                        }
                    }
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    val currentMediaId = p.currentMediaItem?.mediaId ?: LAST_MEDIA_ID_DEFAULT
                    if (currentMediaId == lastMediaId || !isMediaItemsRestored || isFirstSet) {
                        isFirstSet = false
                        return
                    }
                    lastMediaId = currentMediaId
                    /*---------------------------------------------------------------------*/
                    Log.d(TAG, "onMediaMetadataChanged() ${mediaMetadata.title} ${p.currentMediaItem?.localConfiguration?.uri}")
                    mediaMetadataChangedListener?.invoke(mediaMetadata)
                    saveCurrentMediaItemIndex(p.currentMediaItemIndex)
                    saveCurrentPosition(0)
                    setMediaPlaybackState(p.playbackState, if (p.isPlaying) p.playbackParameters.speed else 0f)
                    /*---------------------------------------------------------------------*/
                    Picasso.get()
                        .load(mediaMetadata.artworkUri)
                        .into(object : com.squareup.picasso.Target {
                            override fun onBitmapLoaded(
                                artwork: Bitmap,
                                from: Picasso.LoadedFrom?
                            ) {
                                Log.d(TAG, "onBitmapLoaded() bitmap size: ${artwork.byteCount}")
                                notificationLargeIconBitmap = artwork
                                val notification = createMediaNotification(p.mediaMetadata, artwork, p.isPlaying)
                                showNotification(notification)
                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                e?.printStackTrace()
                            }

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            }
                        })
                }
            })
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        intent?.let {
            MediaButtonReceiver.handleIntent(mediaSession, it)
            if (NotificationDeleteReceiver.checkIntent(it)) {
                Log.d(TAG,"stop self")
                notificationDeletedListener?.invoke()
                MediaItemStorage.clear(baseContext)
                stopSelf()
            }
        }

        if (!isMediaItemsRestored && !getBoundFlag() && tryRestoreMediaItems()) {
            Log.d(TAG, "tryRestoreMediaItems(): true")
            isMediaItemsRestored = true
            transportControls?.let {
                it.skipToQueueItem(getCurrentMediaItemIndex().toLong())
                it.prepare()
                it.seekTo(getCurrentPosition())
            }
            cancelNotification()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind")
        saveBoundFlag(true)
        return binder
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind")
        saveBoundFlag(true)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind")
        saveBoundFlag(false)
        mediaMetadataChangedListener = null
        notificationDeletedListener = null
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        mediaSession?.release()
        player?.release()
    }

    private fun initPlayer() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(com.google.android.exoplayer2.C.USAGE_MEDIA)
            .setContentType(com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player = ExoPlayer.Builder(baseContext)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }

    private fun initMediaSession() {
        mediaSession = MediaSessionCompat(baseContext, TAG).apply {
            setPlaybackState(playbackStateBuilder.build())
            setRatingType(RATING_NONE)
            isActive = true
        }
    }

    private fun initTransportController() {
        transportControls = mediaSession?.controller?.transportControls
    }

    private fun setMediaPlaybackState(playbackState: Int, playbackSpeed: Float) {
        mediaSession?.let { m ->
            player?.let { p ->
                m.setPlaybackState(
                    playbackStateBuilder
                        .setState(
                            playbackState,
                            p.currentPosition,
                            playbackSpeed
                        )
                        .build()
                )
            }
        }
    }

    private fun createMediaNotification(mediaMetadata: MediaMetadata, artwork: Bitmap, isPlaying: Boolean) =
        MediaStyleNotification.createNotification(
            baseContext,
            mediaSession!!.sessionToken,
            mediaMetadata,
            artwork,
            isPlaying
        )

    private fun showNotification(notification: Notification?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(MediaStyleNotification.NOTIFICATION_ID, notification)
    }

    private fun cancelNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(MediaStyleNotification.NOTIFICATION_ID)
    }

    private fun tryRestoreMediaItems(): Boolean {
        val mediaItems = MediaItemStorage.get(baseContext)
        mediaItems?.let { items ->
            player?.let { p ->
                p.setMediaItems(items)
                return true
            }
        }
        return false
    }

    private fun saveCurrentMediaItemIndex(index: Int) =
        preferences
            .edit()
            .putInt(PREF_KEY_CURRENT_MEDIA_ITEM_INDEX, index)
            .apply()

    private fun getCurrentMediaItemIndex(): Int =
        preferences
            .getInt(PREF_KEY_CURRENT_MEDIA_ITEM_INDEX, 0)

    private fun saveCurrentPosition(positionMs: Long) =
        preferences
            .edit()
            .putLong(PREF_KEY_CURRENT_POSITION, positionMs)
            .apply()

    private fun getCurrentPosition(): Long =
        preferences
            .getLong(PREF_KEY_CURRENT_POSITION, 0)

    private fun saveBoundFlag(isBound: Boolean) =
        preferences
            .edit()
            .putBoolean(PREF_KEY_FLAG_BOUND, isBound)
            .apply()

    private fun getBoundFlag(): Boolean =
        preferences
            .getBoolean(PREF_KEY_FLAG_BOUND, false)

    private fun updateSessionMetadata() {
        player?.let { p ->
            Log.d(TAG, "updateSessionMetadata() p.duration: ${p.duration}")
            val metadata = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, p.mediaMetadata.title.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, p.mediaMetadata.artist.toString())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, if (p.duration > 0) p.duration else 1)
                .build()
            mediaSession?.setMetadata(metadata)
        }
    }
}