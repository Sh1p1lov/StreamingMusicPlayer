package com.sh1p1lov.streamingmusicplayer.domain.usecase

import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.repository.MusicServiceRepository

class GetTracksUseCase(private val musicServiceRepository: MusicServiceRepository) {
    suspend fun execute(musicTracksRequestParameters: MusicTracksRequestParameters) =
        musicServiceRepository.getTracks(musicTracksRequestParameters)
}