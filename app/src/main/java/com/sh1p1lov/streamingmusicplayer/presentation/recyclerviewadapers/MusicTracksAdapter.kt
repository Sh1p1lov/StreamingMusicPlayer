package com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.databinding.MusicTrackRecyclerviewItemBinding
import com.sh1p1lov.streamingmusicplayer.domain.models.MusicTrack
import com.squareup.picasso.Picasso

class MusicTracksAdapter : RecyclerView.Adapter<MusicTracksAdapter.MusicTrackViewHolder>() {

    companion object {
        const val DEFAULT_SELECTED_ITEM_INDEX = -1
    }

    private var musicTracksList: List<MusicTrack> = emptyList()
    private var itemClickListener: (playlist: List<MusicTrack>, musicTrackIndex: Int) -> Unit =
        { _, _ -> }
    private var currentSelectedItemIndex = DEFAULT_SELECTED_ITEM_INDEX

    inner class MusicTrackViewHolder(
        val binding: MusicTrackRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (musicTracksList[layoutPosition].isSelected) return@setOnClickListener
                itemClickListener(musicTracksList, layoutPosition)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(musicTracks: List<MusicTrack>) {
        musicTracksList = musicTracks
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(l: (playlist: List<MusicTrack>, musicTrackIndex: Int) -> Unit) {
        itemClickListener = l
    }

    fun select(mediaId: String) {
        unselect()
        val track = musicTracksList.find {
            mediaId == it.id
        }
        track?.let {
            it.isSelected = true
            currentSelectedItemIndex = musicTracksList.indexOf(track)
            notifyItemChanged(currentSelectedItemIndex)
        }
    }

    fun unselect() {
        if (currentSelectedItemIndex != DEFAULT_SELECTED_ITEM_INDEX) {
            musicTracksList[currentSelectedItemIndex].isSelected = false
            notifyItemChanged(currentSelectedItemIndex)
            currentSelectedItemIndex = DEFAULT_SELECTED_ITEM_INDEX
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicTrackViewHolder {
        return MusicTrackViewHolder(
            MusicTrackRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicTrackViewHolder, position: Int) {
        holder.binding.trackName.text = musicTracksList[position].name
        holder.binding.artistName.text = musicTracksList[position].artist_name
        Picasso.get().load(musicTracksList[position].album_image)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(holder.binding.albumImage)
        holder.binding.root.isSelected = musicTracksList[position].isSelected
    }

    override fun getItemCount(): Int {
        return musicTracksList.size
    }
}