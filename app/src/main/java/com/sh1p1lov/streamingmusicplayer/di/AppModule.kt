package com.sh1p1lov.streamingmusicplayer.di

import com.sh1p1lov.streamingmusicplayer.databinding.FragmentMainBinding
import com.sh1p1lov.streamingmusicplayer.domain.usecase.GetTracksUseCase
import com.sh1p1lov.streamingmusicplayer.presentation.MusicTracksAdapter
import com.sh1p1lov.streamingmusicplayer.presentation.mainfragment.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainFragmentViewModel(get())
    }

    factory {
        GetTracksUseCase(get())
    }

    factory {
        MusicTracksAdapter()
    }
}