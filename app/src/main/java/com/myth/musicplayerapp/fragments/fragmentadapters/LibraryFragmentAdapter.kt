package com.myth.musicplayerapp.fragments.fragmentadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.myth.musicplayerapp.R
import com.myth.musicplayerapp.databinding.MusicCardLayoutBinding
import com.myth.musicplayerapp.room.Music

class LibraryFragmentAdapter(
    private val musicList: List<Music>,
    private val clickLister: (Music) -> Unit
) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: MusicCardLayoutBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.music_card_layout,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(musicList[position], clickLister)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }


}

class MyViewHolder(val binding: MusicCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(music: Music, clickLister: (Music) -> Unit) {
        binding.musicTitleText.text = music.title
        binding.musicArtistText.text = music.artist
        binding.musicAlbumText.text = music.album

        binding.musicListItem.setOnClickListener {
            clickLister(music)
        }
    }
}