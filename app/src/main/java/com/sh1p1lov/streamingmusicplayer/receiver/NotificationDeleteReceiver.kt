package com.sh1p1lov.streamingmusicplayer.receiver

import android.content.*
import com.sh1p1lov.streamingmusicplayer.media.MusicPlayerService

private const val TAG = "NotificationDeleteReceiver"

class NotificationDeleteReceiver : BroadcastReceiver() {

    companion object {
        private const val EXTRA_KEY_NOTIFICATION_ACTION = "EXTRA_KEY_NOTIFICATION_DELETE"
        private const val ACTION_NOTIFICATION_DELETE = 0

        fun checkIntent(intent: Intent): Boolean =
            intent.getIntExtra(EXTRA_KEY_NOTIFICATION_ACTION, -1) == ACTION_NOTIFICATION_DELETE
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Intent(context, MusicPlayerService::class.java).also {
            it.putExtra(EXTRA_KEY_NOTIFICATION_ACTION, ACTION_NOTIFICATION_DELETE)
            context?.startService(it)
        }
    }
}