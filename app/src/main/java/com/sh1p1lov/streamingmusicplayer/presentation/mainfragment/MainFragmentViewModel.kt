package com.sh1p1lov.streamingmusicplayer.presentation.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.AudioFormat
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.DateBetween
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.ImageSize
import com.sh1p1lov.streamingmusicplayer.domain.enums.musictracksrequestparameters.MusicTracksOrder
import com.sh1p1lov.streamingmusicplayer.domain.models.Genre
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTracksRequestParameters
import com.sh1p1lov.streamingmusicplayer.domain.usecase.GetTracksUseCase
import com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.models.mainfragmentadapter.MainFragmentAdapterData
import kotlinx.coroutines.*
import java.util.*

class MainFragmentViewModel(
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {

    private val mutableAdapterDataList = MutableLiveData<MainFragmentAdapterData>()
    val adapterDataList: LiveData<MainFragmentAdapterData> = mutableAdapterDataList

    private val mutableIsPageLoaded = MutableLiveData<Boolean>()
    val isPageLoaded: LiveData<Boolean> = mutableIsPageLoaded

    companion object {
        private const val LIMIT = 10
        private const val GENRE_LIMIT = 4
        private const val MONTH_COUNT = 6
        private const val POP_TAG = "pop"
        private const val ROCK_TAG = "rock"
        private const val METAL_TAG = "metal"
        private const val INDIE_TAG = "indie"
        private const val ELECTRONIC_TAG = "electronic"
        private const val HIPHOP_TAG = "hip-hop"
        private const val DANCE_TAG = "dance"
        private const val RNB_TAG = "rnb"
        private const val JAZZ_TAG = "jazz"
        private const val BLUES_TAG = "blues"
        private const val REGGAE_TAG = "reggae"
        private const val PUNK_TAG = "punk"
        private const val COUNTRY_TAG = "country"
        private const val CLASSICAL_TAG = "classical"
    }

    fun loadPage() {
        viewModelScope.launch {
            val tracks = async(Dispatchers.IO) { getTop10NewTracks() }
            val genres = async(Dispatchers.Default) { getGenresList() }

            mutableAdapterDataList.value = MainFragmentAdapterData(
                tracks.await(),
                genres.await()
            )
            mutableIsPageLoaded.value = true
        }
    }

    private suspend fun getTop10NewTracks(): List<MusicTrack> {
        val dateTo = Date()
        val calendar = Calendar.getInstance()
        calendar.time = dateTo
        calendar.add(Calendar.MONTH, -MONTH_COUNT)
        val dateFrom = calendar.time
        val dateBetween = DateBetween(dateFrom, dateTo)

        val musicTracksRequestParameters =
            MusicTracksRequestParameters(
                MusicTracksOrder.POPULARITY_TOTAL,
                LIMIT,
                AudioFormat.MP32,
                dateBetween,
                "",
                ImageSize.SIZE_100
            )

        return getTracksUseCase.execute(musicTracksRequestParameters)
    }

    private suspend fun getGenresList(): List<Genre> {
        viewModelScope.apply {
            val pop = async(Dispatchers.IO) { getGenre(POP_TAG) }
            val rock = async(Dispatchers.IO) { getGenre(ROCK_TAG) }
            val metal = async(Dispatchers.IO) { getGenre(METAL_TAG) }
            val indie = async(Dispatchers.IO) { getGenre(INDIE_TAG) }
            val electronic = async(Dispatchers.IO) { getGenre(ELECTRONIC_TAG) }
            val hiphop = async(Dispatchers.IO) { getGenre(HIPHOP_TAG) }
            val dance = async(Dispatchers.IO) { getGenre(DANCE_TAG) }
            val rnb = async(Dispatchers.IO) { getGenre(RNB_TAG) }
            val jazz = async(Dispatchers.IO) { getGenre(JAZZ_TAG) }
            val blues = async(Dispatchers.IO) { getGenre(BLUES_TAG) }
            val reggae = async(Dispatchers.IO) { getGenre(REGGAE_TAG) }
            val punk = async(Dispatchers.IO) { getGenre(PUNK_TAG) }
            val country = async(Dispatchers.IO) { getGenre(COUNTRY_TAG) }
            val classical = async(Dispatchers.IO) { getGenre(CLASSICAL_TAG) }

            return awaitAll(
                pop,
                rock,
                metal,
                indie,
                electronic,
                hiphop,
                dance,
                rnb,
                jazz,
                blues,
                reggae,
                punk,
                country,
                classical
            )
        }
    }

    private suspend fun getGenre(tag: String): Genre {
        val musicTracksRequestParameters =
            MusicTracksRequestParameters(
                MusicTracksOrder.RELEVANCE,
                GENRE_LIMIT,
                AudioFormat.MP32,
                DateBetween(),
                tag,
                ImageSize.SIZE_100
            )

        val list = getTracksUseCase.execute(musicTracksRequestParameters)

        return Genre(
            name = tag,
            image1 = list[0].album_image,
            image2 = list[1].album_image,
            image3 = list[2].album_image,
            image4 = list[3].album_image
        )
    }
}