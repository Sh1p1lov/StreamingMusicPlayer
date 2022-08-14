package com.sh1p1lov.streamingmusicplayer.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioAttributes
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.databinding.FragmentTestExoBinding

class TestExoFragment : Fragment(R.layout.fragment_test_exo) {

    private lateinit var binding: FragmentTestExoBinding
    private var player: ExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTestExoBinding.bind(view)

        val loadControl = DefaultLoadControl.Builder()
            .setBackBuffer(600000, false)
            .setBufferDurationsMs(1000, 600000, 1000, 1000)
            .build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        player = ExoPlayer.Builder(requireContext())
            .setAudioAttributes(audioAttributes, true)
            .setLoadControl(loadControl)
            .build()
        binding.playerView.player = player
        val mediaItem =
            MediaItem
                .fromUri(
                    "https://prod-1.storage.jamendo.com//?trackid=1848357&format=mp31&from=%2F0wKrCA8uSLMUHmSk5ng4g%3D%3D%7C8MiJUJFmKURws3yao1DS1w%3D%3D"
                )
        player?.addMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}