package com.sh1p1lov.streamingmusicplayer.domain.usecase

import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.repository.MusicServiceRepository

class GetTracksUseCase(private val musicServiceRepository: MusicServiceRepository) {
    fun execute(
        musicTracksRequestParameters: MusicTracksRequestParameters,
        onResponse: (List<MusicTrack>) -> Unit,
        onFailure: (errorMsg: String) -> Unit
    ) {
        musicServiceRepository
            .getTracks(
                musicTracksRequestParameters,
                onResponse,
                onFailure
            )
    }
}