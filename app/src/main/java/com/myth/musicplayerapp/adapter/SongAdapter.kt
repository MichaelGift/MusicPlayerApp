package com.myth.musicplayerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.databinding.MusicCardLayoutBinding
import com.myth.musicplayerapp.models.Song
import com.myth.musicplayerapp.viewmodel.SongViewModel

class SongAdapter(val activity: MainActivity) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private lateinit var songViewModel: SongViewModel

    class SongViewHolder(val itemBinding: MusicCardLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.songTitle == newItem.songTitle
                    && oldItem.songAlbum == newItem.songAlbum
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            MusicCardLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false,
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = differ.currentList[position]

        holder.itemBinding.musicTitleText.text = currentSong.songTitle
        holder.itemBinding.musicArtistText.text = currentSong.songArtist
        holder.itemBinding.musicAlbumText.text = currentSong.songAlbum

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "You chose to play ${currentSong.songTitle}",
                Toast.LENGTH_LONG
            ).show()
            songViewModel = (activity as MainActivity).songViewModel
            songViewModel.playSong(currentSong)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}