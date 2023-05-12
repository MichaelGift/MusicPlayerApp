package com.myth.musicplayerapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myth.musicplayerapp.data.models.Song
import com.myth.musicplayerapp.repository.utility.FileManager
import com.myth.musicplayerapp.repository.utility.SharedPreferencesManager
import com.myth.musicplayerapp.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongViewModel(
    private val songRepository: SongRepository
) : ViewModel() {
    val sharedPreferencesManager = SharedPreferencesManager()
    val fileManager = FileManager()
    lateinit var selectedSong: Song
    var openedPlaylistName = ""
    private var playlistAllSongs: ArrayList<Song> = ArrayList()

    fun clearPlaylist() {
        playlistAllSongs.clear()
    }

    fun addPlaylistItem(song: Song) {
        playlistAllSongs.add(song)
    }

    fun getPlaylist(): ArrayList<Song> {
        return playlistAllSongs
    }

    fun getAllSongs() = songRepository.getAllSongs()
    fun getAllFavoriteSongs() = songRepository.getAllFavoriteSongs()
    fun getAllPlaylistSongs(playListName: String) = songRepository.getPlaylistSongs(playListName)
    fun getAllRecentPlayedSongs(limit: Int) = songRepository.getRecentPlayedSongs(limit)

    fun clearAllFavoriteSongs(callback: () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            songRepository.clearFavorites()
            withContext(Dispatchers.Main) {
                callback()
            }
        }

    fun clearPlaylistSongs(playListName: String, callback: () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            songRepository.clearPlaylistSongs(playListName)
            withContext(Dispatchers.Main) {
                callback()
            }
        }

    fun removeSong(song: Song, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        songRepository.deleteSong(song)
        withContext(Dispatchers.Main) {
            callback()
        }
    }

    fun addSong(song: Song, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        songRepository.insertSong(song)
        withContext(Dispatchers.Main) {
            callback()
        }
    }

    fun updateSong(song: Song, callback: () -> Unit) = viewModelScope.launch(Dispatchers.Default) {
        songRepository.updateSong(song)
        withContext(Dispatchers.Main) {
            callback()
        }
    }

    fun saveAllSongsToDatabase(context: Context, callback: () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            val audioFiles = fileManager.getAllAudioFiles(context)
            val time = System.currentTimeMillis()

            for (song in audioFiles) {
                song.lastPlayed = time
                songRepository.insertSong(song)
            }
            withContext(Dispatchers.Main) {
                callback()
            }
        }
}