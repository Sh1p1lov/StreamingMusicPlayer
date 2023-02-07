package com.sh1p1lov.streamingmusicplayer.presentation.mainfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.*
import androidx.core.view.doOnLayout
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.transition.*
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.databinding.FragmentMainBinding
import com.sh1p1lov.streamingmusicplayer.presentation.mainactivity.MusicPlayer
import com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.MusicGenresAdapter
import com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.MusicTracksAdapter
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        private const val TAG = "MainFragmentLog"
    }

    private lateinit var binding: FragmentMainBinding
    private val vm: MainFragmentViewModel by viewModel()
    private val musicTracksAdapter: MusicTracksAdapter = get()
    private val musicGenresAdapter: MusicGenresAdapter = get()
    private val musicPlayer by lazy { activity as MusicPlayer }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        Log.d(TAG, object{}.javaClass.enclosingMethod?.name.toString())

        if (vm.isPageLoaded.value == null) {
            binding.progressBar.visibility = View.VISIBLE
            binding.content.visibility = View.GONE
            vm.loadPage()
        }

        vm.isPageLoaded.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        }

        vm.adapterDataList.observe(viewLifecycleOwner) {
            binding.recyclerTracks.adapter = musicTracksAdapter
            musicTracksAdapter.setList(it.tracks)
            musicTracksAdapter.select(musicPlayer.getCurrentMediaId())
            binding.recyclerGenre.adapter = musicGenresAdapter
            musicGenresAdapter.setList(it.genres)
        }

        postponeEnterTransition()
        binding.recyclerGenre.doOnLayout {
            startPostponedEnterTransition()
        }

        musicGenresAdapter.setOnClickListener { v ->
            exitTransition = Explode().apply {
                duration = 200
                interpolator = DecelerateInterpolator()
            }

            val action = MainFragmentDirections.actionMainFragmentToGenreFragment(v.transitionName)
            findNavController()
                .navigate(
                    action,
                    FragmentNavigator.Extras.Builder()
                        .addSharedElements(mapOf(
                            v to v.transitionName
                        ))
                        .build()
                )
        }

        musicTracksAdapter.setOnItemClickListener { playlist, musicTrackIndex ->
            musicPlayer.setMusicPlaylist(playlist, musicTrackIndex)
        }

        musicPlayer.setMediaIdUpdatedListener {
            musicTracksAdapter.select(it)
        }

        musicPlayer.setStoppedListener {
            musicTracksAdapter.unselect()
        }
    }
}