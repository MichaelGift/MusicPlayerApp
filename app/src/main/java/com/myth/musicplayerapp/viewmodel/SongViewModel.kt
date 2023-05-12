package com.myth.musicplayerapp.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.myth.musicplayerapp.models.Song
import com.myth.musicplayerapp.repository.SongRepository
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SongViewModel(
    val app: Application,
    private val songRepository: SongRepository
) : AndroidViewModel(app) {

    var songCurrentTime: Long = 0
    var songDuration: Int = 0
    var seekBarMax: Int = 0

    private var forwardTime = 10000
    private var rewindTime = 10000

    var mediaPlayer: MediaPlayer? = null
    private var handler: Handler = Handler()

    private var currentSongPlaying: Song? = null

    val songsOnDevice = songRepository.songLiveData


    private var _formattedSongCurrentTime = MutableLiveData<String>()
    val formattedSongCurrentTime: LiveData<String>
        get() = _formattedSongCurrentTime

    private var _formattedSongDuration = MutableLiveData<String>()
    val formattedSongDuration: LiveData<String>
        get() = _formattedSongDuration

    private var _seekBarProgress = MutableLiveData<Int>()
    val seekBarProgress: LiveData<Int>
        get() = _seekBarProgress

    private var _songTitle = MutableLiveData<String>()
    val songTitle: LiveData<String>
        get() = _songTitle

    fun updateSong(song: Song) = viewModelScope.launch {
        songRepository.updateSong(song)
    }

    fun deleteSong(song: Song) = viewModelScope.launch {
        songRepository.deleteSong(song)
    }

    fun getAllSongsOnDevice() = songRepository.getAllSongsOnDevice()
    fun getAllSongs() = songRepository.getAllSongs()
    fun searchSongs(query: String) = songRepository.searchSongs(query)

    fun playSelectedSong(song: Song) {

        println("Playing ${song.songTitle} now")
        if (currentSongPlaying == null) {

            currentSongPlaying = song
            createMusicPlayer()
            mediaPlayer?.start()

            songDuration = song.songDuration.toInt()
            _formattedSongDuration.value = formatTime(songDuration.toLong())
            _songTitle.value = song.songTitle

            mediaPlayer?.setOnCompletionListener {
                currentSongPlaying = null
            }
        } else {
            mediaPlayer?.stop()

            currentSongPlaying = song
            createMusicPlayer()
            mediaPlayer?.start()

            songDuration = song.songDuration.toInt()
            seekBarMax = songDuration
            _songTitle.value = song.songTitle

            _formattedSongDuration.value = formatTime(songDuration.toLong())

            mediaPlayer?.setOnCompletionListener {
                currentSongPlaying = null
            }
        }
        handler.postDelayed(updateSongTime, 100)
    }

    private fun createMusicPlayer() {
        mediaPlayer = MediaPlayer.create(app.applicationContext, currentSongPlaying?.contentUri)
    }

    fun playSong() {

        if (mediaPlayer != null) {
            mediaPlayer?.start()
        } else {
            Toast.makeText(
                app.applicationContext,
                "Choose a song",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            println("Song Paused")
        }
        println("The song has a length of ${mediaPlayer?.duration}")
    }

    fun rewindSong() {
        val temp = songCurrentTime.toInt()

        if ((temp - rewindTime) > 0) {
            songCurrentTime -= rewindTime
            mediaPlayer?.seekTo(songCurrentTime.toInt())
        } else {
            Toast.makeText(
                app.applicationContext,
                "Cannot Jump to before Song Started",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun forwardSong() {
        val temp = songCurrentTime.toInt()

        if ((temp + forwardTime) <= songDuration) {
            songCurrentTime += forwardTime
            mediaPlayer?.seekTo(songCurrentTime.toInt())
        } else {
            Toast.makeText(
                app.applicationContext,
                "Cannot Jump to before Song Started",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            songCurrentTime = mediaPlayer?.currentPosition!!.toDouble().toLong()

            seekBarMax = mediaPlayer?.duration!!.toInt()
            _seekBarProgress.value = songCurrentTime.toInt()
            _formattedSongCurrentTime.value = formatTime(songCurrentTime)

            handler.postDelayed(this, 100)
        }
    }

    fun formatTime(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

}