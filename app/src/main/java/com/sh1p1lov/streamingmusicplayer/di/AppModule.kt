package com.sh1p1lov.streamingmusicplayer.di

import com.sh1p1lov.streamingmusicplayer.domain.usecase.GetTracksUseCase
import com.sh1p1lov.streamingmusicplayer.presentation.genrefragment.GenreFragmentViewModel
import com.sh1p1lov.streamingmusicplayer.presentation.mainactivity.MainActivityViewModel
import com.sh1p1lov.streamingmusicplayer.presentation.mainfragment.MainFragmentViewModel
import com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.MusicGenresAdapter
import com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.MusicTracksAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainFragmentViewModel(get())
    }

    viewModel {
        MainActivityViewModel()
    }

    viewModel {
        GenreFragmentViewModel(get())
    }

    factory {
        GetTracksUseCase(get())
    }

    factory {
        MusicTracksAdapter()
    }

    factory {
        MusicGenresAdapter()
    }
}