package com.myth.musicplayerapp.repository.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.myth.musicplayerapp.data.models.Song
import com.myth.musicplayerapp.repository.utility.PlayType
import java.io.File

class MusicPlaybackService : Service() {

    private var musicPlayType = PlayType.Linear
    private val musicPlaylist = ArrayList<Song>()
    private var currentMusicPlaying: Song? = null
    private var player: MediaPlayer? = null
    private var isServiceRunning = false
    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicPlaybackService = this@MusicPlaybackService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isServiceRunning = true
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        isServiceRunning = false
    }

    private fun getSongIndex(song: Song): Int {
        for (index in 0 until musicPlaylist.size) {
            if (song.audioPath == musicPlaylist[index].audioPath) {
                return index
            }
        }
        return -1
    }


    private fun sendMusicChangeBroadcast() {
        val intent = Intent()
        intent.action = "android.intent.action.MUSIC_STATE_CHANGE"
        sendBroadcast(intent)
    }

    private fun sendPlayPauseBroadcast() {
        val intent = Intent()
        intent.action = "android.intent.action.MUSIC_PLAY_PAUSE_STATE_CHANGE"
        sendBroadcast(intent)
    }

    fun playNewMusic(song: Song) {
        println("${song.title} playing right now")
        try {
            if (currentMusicPlaying != null) {
                if (currentMusicPlaying!!.audioPath == song.audioPath) return
            }
            currentMusicPlaying = song

            val uri = Uri.fromFile(File(song.audioPath))
            if (player != null && player!!.isPlaying) {
                player!!.stop()
                player!!.release()
            }

            player = MediaPlayer.create(this.applicationContext, uri)
            player!!.isLooping = false
            player!!.start()

            player!!.setOnCompletionListener {
                sendPlayPauseBroadcast()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("tag", "Unable to play music")
        }
        sendMusicChangeBroadcast()
    }

    fun resumeMusic() {
        player?.start()
        sendPlayPauseBroadcast()
    }

    fun pauseMusic() {
        player?.pause()
        sendPlayPauseBroadcast()
    }

    fun isMusicPlaying(): Boolean {
        if (player == null) return false
        return player!!.isPlaying
    }

    fun getPlayingMusicTitle(): String {
        if (currentMusicPlaying == null) {
            return "Empty"
        }
        return currentMusicPlaying!!.audioPath
    }

    fun getPlayingMusicArtist(): String {
        if (currentMusicPlaying == null) {
            return "Empty"
        }
        return currentMusicPlaying!!.artist
    }

    fun getMusicDuration(): Int {
        return if (player == null) 0 else player!!.duration
    }

    fun setMusicSeek(seekTo: Int) {
        if (player != null) player!!.seekTo(seekTo)
    }

    fun getMusicSeek() : Int{
        return if (player != null) player!!.currentPosition else 0
    }

    fun setMusicPlaylist(musicPaths: ArrayList<Song>) {
        musicPlaylist.clear()
        musicPlaylist.addAll(musicPlaylist)
    }

    fun forwardMusic(): Boolean {
        if (hasFowardedMusic()) {
            playNewMusic(musicPlaylist[getSongIndex(currentMusicPlaying!!) + 1])
            return true
        }
        return false
    }

    fun rewindMusic(): Boolean {
        if (hasRewindedMusic()) {
            playNewMusic(musicPlaylist[getSongIndex(currentMusicPlaying!!) - 1])
            return true
        }
        return false
    }

    private fun hasFowardedMusic(): Boolean {
        if (currentMusicPlaying == null) return false
        val playlistSize = musicPlaylist.size
        if (playlistSize > 0) {
            if (getSongIndex(currentMusicPlaying!!) + 1 < playlistSize) return true
        }
        return false
    }

    private fun hasRewindedMusic(): Boolean {
        if (currentMusicPlaying == null) return false
        val playlistSize = musicPlaylist.size
        if (playlistSize > 0) {
            if (getSongIndex(currentMusicPlaying!!) - 1 > -1) return true
        }
        return false
    }

    fun setMusicPlayType(playType: PlayType) {
        musicPlayType = playType
    }

    fun isServiceRunning(): Boolean {
        return isServiceRunning
    }
}