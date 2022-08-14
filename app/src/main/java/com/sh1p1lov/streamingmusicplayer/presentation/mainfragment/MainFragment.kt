package com.sh1p1lov.streamingmusicplayer.presentation.mainfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.databinding.FragmentMainBinding
import com.sh1p1lov.streamingmusicplayer.presentation.MusicTracksAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by lazy { FragmentMainBinding.bind(view as View) }
    private val vm: MainFragmentViewModel by viewModel()
    private val adapter: MusicTracksAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.top10Tracks.layoutManager = LinearLayoutManager(context)
        binding.top10Tracks.adapter = adapter

        if (vm.musicTracksList.value.isNullOrEmpty()) {
            vm.getTop10TracksOfMonth()
        }
        vm.musicTracksList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }
}