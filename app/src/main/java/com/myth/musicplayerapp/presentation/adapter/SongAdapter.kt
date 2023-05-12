package com.myth.musicplayerapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.data.models.Song
import com.myth.musicplayerapp.databinding.MusicCardLayoutBinding
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel
import com.myth.musicplayerapp.repository.service.MusicPlaybackService

class SongAdapter(private val activity: MainActivity) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private lateinit var songViewModel: SongViewModel
    private var musicService: MusicPlaybackService? = null

    class SongViewHolder(val itemBinding: MusicCardLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.title == newItem.title
                    && oldItem.albumId == newItem.albumId
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

        holder.itemBinding.musicTitleText.text = currentSong.title
        holder.itemBinding.musicArtistText.text = currentSong.artist
        holder.itemBinding.musicAlbumText.text = currentSong.albumId.toString()

        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "You chose to play ${currentSong.title}",
                Toast.LENGTH_LONG
            ).show()
            songViewModel = activity.songViewModel
            songViewModel.clearPlaylist()
            for (songData in differ.currentList) {
                songViewModel.addPlaylistItem(songData)
            }
            songViewModel.selectedSong = currentSong
            songViewModel.openedPlaylistName = "Recent"

            musicService = activity.getMusicService()
            musicService?.playNewMusic(currentSong)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}