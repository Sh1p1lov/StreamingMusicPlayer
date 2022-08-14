package com.sh1p1lov.streamingmusicplayer.di

import com.sh1p1lov.streamingmusicplayer.data.api.repository.MusicServiceRepositoryImpl
import com.sh1p1lov.streamingmusicplayer.domain.repository.MusicServiceRepository
import org.koin.dsl.module

val dataModule = module {
    single<MusicServiceRepository> {
        MusicServiceRepositoryImpl()
    }
}