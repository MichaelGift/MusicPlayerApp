package com.myth.musicplayerapp.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myth.musicplayerapp.models.Song
import com.myth.musicplayerapp.repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(
    val app: Application,
    private val songRepository: SongRepository
) : AndroidViewModel(app) {

    private var songCurrentTime = 0.0
    private var songDuration : Int = 0
    private var forwardTime = 10000
    private var rewindTime = 10000

    private lateinit var mediaPlayer: MediaPlayer

    private var currentSongPlaying : Song? = null

    val songsOnDevice = songRepository.songLiveData

    fun updateSong(song: Song) = viewModelScope.launch {
        songRepository.updateSong(song)
    }

    fun deleteSong(song: Song) = viewModelScope.launch {
        songRepository.deleteSong(song)
    }

    fun getAllSongsOnDevice() = songRepository.getAllSongsOnDevice()
    fun getAllSongs() = songRepository.getAllSongs()
    fun searchSongs(query: String) = songRepository.searchSongs(query)

    fun playSong(song: Song) {
        if(currentSongPlaying == null){
            mediaPlayer = MediaPlayer.create(app.applicationContext,song.contentUri)
            mediaPlayer.start()
            currentSongPlaying = song
            songDuration = song.songDuration.toInt()

            mediaPlayer.setOnCompletionListener {
                currentSongPlaying =null
            }
        }else{
            Toast.makeText(
                app.applicationContext,
                "There's another song playing",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun pauseSong() {
        mediaPlayer.pause()
    }

    fun rewindSong() {
        val temp = songCurrentTime.toInt()

        if((temp - rewindTime)> 0){
            songCurrentTime -= rewindTime
            mediaPlayer.seekTo(songCurrentTime.toInt())
        }
        else{
            Toast.makeText(
                app.applicationContext,
                "Cannot Jump to before Song Started",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun forwardSong() {
        val temp = songCurrentTime.toInt()

        if((temp + forwardTime)<= songDuration){
            songCurrentTime += forwardTime
            mediaPlayer.seekTo(songCurrentTime.toInt())
        }
        else{
            Toast.makeText(
                app.applicationContext,
                "Cannot Jump to before Song Started",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}