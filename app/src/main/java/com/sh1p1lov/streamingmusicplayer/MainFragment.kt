package com.sh1p1lov.streamingmusicplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.sh1p1lov.streamingmusicplayer.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
    }
}