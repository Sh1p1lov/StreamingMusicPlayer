package com.sh1p1lov.streamingmusicplayer.presentation.mainactivity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sh1p1lov.streamingmusicplayer.databinding.ActivityMainBinding
import com.sh1p1lov.streamingmusicplayer.databinding.MiniPlayerControllerBinding
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.sh1p1lov.streamingmusicplayer.media.MusicPlayerService
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

const val TAG = "MainActivityLog"

class MainActivity : AppCompatActivity(), MusicPlayer {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val playerMiniBinding by lazy { MiniPlayerControllerBinding.bind(binding.playerMini.getChildAt(0)) }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.standardBottomSheet) }
    private val vm: MainActivityViewModel by viewModel()
    private val intentMusicPlayerService by lazy { Intent(this, MusicPlayerService::class.java) }
    private var service: MusicPlayerService? = null
    private var mediaIdUpdatedListener: (mediaId: String) -> Unit = {}
    private var serviceStoppedListener = {}

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            Log.d(TAG, "onServiceConnected")
            val binder = service as MusicPlayerService.MusicPlayerServiceBinder
            this@MainActivity.service = binder.getService()
            onMusicPlayerServiceConnected()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            service = null
        }
    }

    private fun onMusicPlayerServiceConnected() {
        service?.let { musicService ->
            val exoplayer = musicService.getPlayer()
            binding.playerMainBinding.playerView.player = exoplayer
            binding.playerMini.player = exoplayer

            musicService.setMediaMetadataChangedListener {
                vm.setCurrentMediaMetadata(it)
            }

            musicService.setNotificationDeletedListener {
                toInitialBottomSheetState()
                serviceStoppedListener.invoke()
            }

            synchronizeWithMusicPlayerService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        restoreLastBottomSheetState()
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        vm.setBottomSheetState(newState)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        vm.setBottomSheetState(newState)
                        bottomSheetBehavior.isHideable = false
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        vm.setBottomSheetState(newState)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.playerMainBinding.root.alpha = slideOffset
                binding.playerMini.alpha = 1 - slideOffset * 2
                binding.btnBottomSheetCollapse.alpha = slideOffset

                if (slideOffset < 0f) { // slideOffset [-1.0; 0.0]
                    val peekH = bottomSheetBehavior.peekHeight
                    val margin = ((1f + slideOffset) * peekH).roundToInt()
                    setMargin(
                        bottom = margin
                    )
                } else {
                    val h = binding.root.height - bottomSheetBehavior.peekHeight
                    val transition = slideOffset * h
                    binding.navHostFragment.translationY = -transition
                }
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.btnBottomSheetCollapse.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        vm.currentMediaMetadata.observe(this) {
            synchronizeWithMusicPlayerService()
        }
    }

    override fun onStart() {
        super.onStart()
        bindMusicPlayerService()
    }

    override fun onStop() {
        super.onStop()
        service?.let {
            service = null
            unbindService(connection)
        }
    }

    override fun setMusicPlaylist(playlist: List<MusicTrack>, musicTrackIndex: Int) {
        startMusicPlayerService()
        service?.setMediaItems(convertToMediaItems(playlist), musicTrackIndex)
    }

    override fun setMediaIdUpdatedListener(l: (mediaId: String) -> Unit) {
        mediaIdUpdatedListener = l
    }

    override fun setStoppedListener(l: () -> Unit) {
        serviceStoppedListener = l
    }

    override fun getCurrentMediaId(): String =
        service?.getCurrentMediaId() ?: ""

    private fun restoreLastBottomSheetState() {
        vm.bottomSheetState.value?.let {
            bottomSheetBehavior.state = it
            when (it) {
                BottomSheetBehavior.STATE_HIDDEN -> {}
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    binding.playerMainBinding.root.alpha = 0f
                    binding.playerMini.alpha = 1f
                    binding.btnBottomSheetCollapse.alpha = 0f
                    setMargin(
                        bottom = bottomSheetBehavior.peekHeight
                    )
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    binding.playerMainBinding.root.alpha = 1f
                    binding.playerMini.alpha = 0f
                    binding.btnBottomSheetCollapse.alpha = 1f
                    setMargin(
                        bottom = bottomSheetBehavior.peekHeight
                    )
                }
            }
        } ?: run {
            toInitialBottomSheetState()
        }
    }

    private fun synchronizeWithMusicPlayerService() {
        service?.let { s ->
            s.getCurrentMediaMetadata()?.let {
                updateMusicMetadata(it, s.getCurrentMediaId())
            }
        }
    }

    private fun updateMusicMetadata(mediaMetadata: MediaMetadata, mediaId: String) {
        with(mediaMetadata) {
            playerMiniBinding.trackName.text = title
            playerMiniBinding.artistName.text = artist
            binding.playerMainBinding.musicInfo.trackName.text = title
            binding.playerMainBinding.musicInfo.artistName.text = artist
            if (mediaMetadata != MediaMetadata.EMPTY && bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                setMargin(
                    bottom = bottomSheetBehavior.peekHeight
                )
            }
            Picasso.get().load(artworkUri).apply {
                into(playerMiniBinding.albumImage)
                into(binding.playerMainBinding.musicInfo.albumImage)
            }
            mediaIdUpdatedListener.invoke(mediaId)
        }
    }

    private fun toInitialBottomSheetState() {
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun startMusicPlayerService() =
        startService(intentMusicPlayerService)

    private fun bindMusicPlayerService() =
        bindService(intentMusicPlayerService, connection, Context.BIND_AUTO_CREATE)

    private fun setMargin(left: Int = -1, top: Int = -1, bottom: Int = -1, right: Int = -1) {
        val params = binding.navHostFragment.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(
            if (left >= 0) left else params.leftMargin,
            if (top >= 0) top else params.topMargin,
            if (right >= 0) right else params.rightMargin,
            if (bottom >= 0) bottom else params.bottomMargin)
        binding.navHostFragment.requestLayout()
    }
}