package com.myth.musicplayerapp.repository

import com.myth.musicplayerapp.data.database.SongDatabase
import com.myth.musicplayerapp.data.models.Song
import kotlinx.coroutines.flow.Flow

class SongRepository(private val db: SongDatabase) {

    suspend fun isTableEmpty(): Boolean {
        val count = db.getSongDao().rowCount()
        return count == 0
    }

    suspend fun clearPlaylistSongs(playlistName: String) =
        db.getSongDao().clearPlaylistSongs(playlistName)

    fun getPlaylistSongs(playlistName: String): Flow<List<Song>> =
        db.getSongDao().getPlaylistSongs(playlistName)

    fun getAllSongs(): Flow<List<Song>> = db.getSongDao().getAllSongs()
    fun getAllFavoriteSongs(): Flow<List<Song>> = db.getSongDao().getAllFavoriteSongs()
    fun getRecentPlayedSongs(limit: Int): Flow<List<Song>> = db.getSongDao().getRecentSongs(limit)
    suspend fun clearFavorites() = db.getSongDao().clearFavoritesSongs()
    suspend fun insertSong(song: Song) = db.getSongDao().insertSong(song)
    suspend fun updateSong(song: Song) = db.getSongDao().updateSong(song)
    suspend fun deleteSong(song: Song) = db.getSongDao().deleteSong(song)
    fun searchSongs(query: String) = db.getSongDao().searchSong(query)
}