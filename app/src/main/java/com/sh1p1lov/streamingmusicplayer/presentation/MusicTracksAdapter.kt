package com.sh1p1lov.streamingmusicplayer.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh1p1lov.streamingmusicplayer.databinding.MusicTrackRecyclerviewItemBinding
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.squareup.picasso.Picasso

class MusicTracksAdapter : RecyclerView.Adapter<MusicTracksAdapter.MusicTrackViewHolder>() {

    private var musicTracksList: List<MusicTrack> = emptyList()

    fun setList(musicTracks: List<MusicTrack>) {
        musicTracksList = musicTracks
        notifyDataSetChanged()
    }

    class MusicTrackViewHolder(
        val binding: MusicTrackRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicTrackViewHolder {
        return MusicTrackViewHolder(
            MusicTrackRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicTrackViewHolder, position: Int) {
        holder.binding.trackName.text = musicTracksList[position].name
        holder.binding.artistName.text = musicTracksList[position].artist_name
        Picasso.get().load(musicTracksList[position].album_image).into(holder.binding.albumImage)
    }

    override fun getItemCount(): Int {
        return musicTracksList.size
    }
}