package com.myth.musicplayerapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.data.models.Song
import com.myth.musicplayerapp.databinding.MusicCardLayoutBinding
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel

class SongAdapter(
    private val activity: MainActivity
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private lateinit var songViewModel: SongViewModel

    class SongViewHolder(val itemBinding: MusicCardLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.albumId == newItem.albumId
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
        /*holder.itemBinding.musicAlbumText.text = currentSong.albumId.toString()*/

        holder.itemView.setOnClickListener {
            //Reference the view model, though this method seems a bit crude
            songViewModel = activity.songViewModel

            //clear the playlist
            songViewModel.clearPlaylist()

            //add all songs in the currentlist to a new playlist
            for (songData in differ.currentList) {
                songViewModel.addPlaylistItem(songData)
            }

            // set this specifics song as the current song
            songViewModel.selectedSong = currentSong

            //create a playlist called "library" since this is a list of songs from the storage
            songViewModel.openedPlaylistName = "Library"

            //get a reference to the playback service
            val musicPlaybackService = activity.getMusicService()

            //Make the service play the current song, this selected song
            musicPlaybackService?.playNewMusic(currentSong)
            musicPlaybackService?.setMusicPlaylist(songViewModel.playlistAllSongs)
            //Debug checks
            /*println("Playlist ${songViewModel.openedPlaylistName.toString()} has ${songViewModel.playlistAllSongs.size} songs")*/
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}