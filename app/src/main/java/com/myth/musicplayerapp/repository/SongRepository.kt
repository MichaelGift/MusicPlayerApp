package com.myth.musicplayerapp.repository

import android.app.Application
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.database.DeviceSongDao
import com.myth.musicplayerapp.database.SongDatabase
import com.myth.musicplayerapp.models.Song

class SongRepository(private val db: SongDatabase, val app: Application, val deviceSongDao: DeviceSongDao) {

    val songLiveData  = deviceSongDao.songLiveData

    suspend fun updateSong(song: Song) = db.getSongDao().updateSong(song)
    suspend fun deleteSong(song: Song) = db.getSongDao().deleteSong(song)

    fun getAllSongs() = db.getSongDao().getAllSongs()
    fun getAllSongsOnDevice() = deviceSongDao.getAllMusicOnDevice(app.applicationContext)
    fun searchSongs(query: String) = db.getSongDao().searchSong(query)
}