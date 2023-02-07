package com.sh1p1lov.streamingmusicplayer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.MediaMetadata
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.presentation.mainactivity.MainActivity
import com.sh1p1lov.streamingmusicplayer.receiver.NotificationDeleteReceiver

internal object MediaStyleNotification {
    const val CHANNEL_ID = "music_player_channel"
    const val NOTIFICATION_ID = 101

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun createNotification(
        context: Context,
        mediaSessionToken: MediaSessionCompat.Token,
        mediaMetadata: MediaMetadata,
        artwork: Bitmap,
        isPlaying: Boolean
    ): Notification {

        val launchIntent = Intent(Intent.ACTION_MAIN, null, context, MainActivity::class.java)
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val deleteIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, NotificationDeleteReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseResId =
            if (isPlaying) R.drawable.exo_ic_pause
            else R.drawable.exo_ic_play

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(mediaMetadata.title)
            .setContentText(mediaMetadata.artist)
            .setLargeIcon(artwork)
            .setSmallIcon(R.drawable.icon_music)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setContentIntent(contentIntent)
            .setDeleteIntent(deleteIntent)
            .addAction(
                R.drawable.btn_skip_previous_48,
                "previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )
            .addAction(
                playPauseResId,
                "play_pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )
            .addAction(
                R.drawable.btn_skip_next_48,
                "next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )

        return notificationBuilder.build()
    }
}