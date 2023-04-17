package com.myth.musicplayerapp.viewmodels

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel

class PlayerFragmentViewModel : ViewModel() {
    var isPlaying: Boolean = false

    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var rewindTime = 10000
    var oneTimeOnly = 0

    private lateinit var mediaPlayer: MediaPlayer

    fun playMusic() {}
    fun pauseMusic() {}
    fun rewindMusic() {}
    fun forwardMusic() {}
    fun nextSong() {}
    fun previousSong() {}
}