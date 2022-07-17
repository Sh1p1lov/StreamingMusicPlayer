package com.sh1p1lov.streamingmusicplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.sh1p1lov.streamingmusicplayer.databinding.FragmentBlankBinding

class BlankFragment : Fragment(R.layout.fragment_blank) {

    lateinit var binding: FragmentBlankBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBlankBinding.bind(view)
    }
}