package com.sh1p1lov.streamingmusicplayer.presentation.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaMetadata

class MainActivityViewModel : ViewModel() {
    private val mutableCurrentMediaMetadata = MutableLiveData<MediaMetadata>()
    val currentMediaMetadata: LiveData<MediaMetadata> = mutableCurrentMediaMetadata

    private val mutableBottomSheetState = MutableLiveData<Int>()
    val bottomSheetState: LiveData<Int> = mutableBottomSheetState

    fun setCurrentMediaMetadata(mediaMetadata: MediaMetadata) {
        mutableCurrentMediaMetadata.value = mediaMetadata
    }

    fun setBottomSheetState(state: Int) {
        mutableBottomSheetState.value = state
    }
}