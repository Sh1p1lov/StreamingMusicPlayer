package com.sh1p1lov.streamingmusicplayer.presentation.genrefragment

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.*
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.databinding.FragmentGenreBinding
import com.sh1p1lov.streamingmusicplayer.presentation.mainactivity.MusicPlayer
import com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers.MusicTracksAdapter
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenreFragment : Fragment(R.layout.fragment_genre) {

    companion object {
        private const val TAG = "GenreFragmentLog"
    }

    private var _binding: FragmentGenreBinding? = null
    private val binding: FragmentGenreBinding get() = _binding!!
    private val musicTracksAdapter: MusicTracksAdapter = get()
    private val args: GenreFragmentArgs by navArgs()
    private val vm: GenreFragmentViewModel by viewModel()
    private val musicPlayer by lazy { activity as MusicPlayer }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGenreBinding.bind(view)

        if (vm.track.value == null) {
            vm.getTracksByTag(args.cardItemTransitionName)
            binding.apply {
                progressBar.visibility = View.VISIBLE
                recyclerTracks.visibility = View.GONE
            }
        }
        vm.track.observe(viewLifecycleOwner) {
            binding.recyclerTracks.adapter = musicTracksAdapter
            musicTracksAdapter.setList(it)
            musicTracksAdapter.select(musicPlayer.getCurrentMediaId())
            binding.apply {
                progressBar.visibility = View.GONE
                recyclerTracks.visibility = View.VISIBLE
            }
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

        with(binding) {
            toolbar.transitionName = args.cardItemTransitionName
            toolbar.title = args.cardItemTransitionName
        }

        sharedElementEnterTransition = TransitionSet()
            .setInterpolator(DecelerateInterpolator())
            .setDuration(300)
            .addTransition(ChangeTransform())

        sharedElementReturnTransition = TransitionSet()
            .setInterpolator(DecelerateInterpolator())
            .setDuration(200)
            .addTransition(ChangeTransform())

        binding.toolbar.setNavigationOnClickListener {
            findNavController()
                .popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}