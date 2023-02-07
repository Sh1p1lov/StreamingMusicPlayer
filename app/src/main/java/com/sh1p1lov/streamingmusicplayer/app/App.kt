package com.sh1p1lov.streamingmusicplayer.app

import android.app.Application
import com.sh1p1lov.streamingmusicplayer.di.appModule
import com.sh1p1lov.streamingmusicplayer.di.dataModule
import com.sh1p1lov.streamingmusicplayer.di.domainModule
import com.sh1p1lov.streamingmusicplayer.notification.MediaStyleNotification
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, domainModule, dataModule)
        }
        MediaStyleNotification.createNotificationChannel(this)
    }
}