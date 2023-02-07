package com.sh1p1lov.streamingmusicplayer.presentation.recyclerviewadapers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh1p1lov.streamingmusicplayer.R
import com.sh1p1lov.streamingmusicplayer.databinding.MusicGenreRecyclerviewItemBinding
import com.sh1p1lov.streamingmusicplayer.domain.models.Genre
import com.squareup.picasso.Picasso

class MusicGenresAdapter : RecyclerView.Adapter<MusicGenresAdapter.MusicGenreViewHolder>() {

    private var genresList: List<Genre> = emptyList()
    private var itemClickListener: (v: View) -> Unit = {}

    inner class MusicGenreViewHolder(
        val binding: MusicGenreRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener.invoke(binding.name)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(genres: List<Genre>) {
        genresList = genres
        notifyDataSetChanged()
    }

    fun setOnClickListener(l: (v: View) -> Unit) {
        itemClickListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicGenreViewHolder {
        return MusicGenreViewHolder(
            MusicGenreRecyclerviewItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: MusicGenreViewHolder, position: Int) {
        holder.binding.name.text = genresList[position].name
        Picasso.get().apply {
            load(genresList[position].image1)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.binding.albumImage1)
            load(genresList[position].image2)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.binding.albumImage2)
            load(genresList[position].image3)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.binding.albumImage3)
            load(genresList[position].image4)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.binding.albumImage4)
        }
        holder.binding.name.transitionName = genresList[position].name
    }

    override fun getItemCount(): Int {
        return genresList.size
    }
}