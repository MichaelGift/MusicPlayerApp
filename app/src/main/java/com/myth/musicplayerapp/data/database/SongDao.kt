package com.myth.musicplayerapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myth.musicplayerapp.data.models.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Insert
    suspend fun insertSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Update
    suspend fun updateSong(song: Song)

    @Query("SELECT * FROM songs ORDER BY title DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE is_favorite = 1")
    fun getAllFavoriteSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE playlist =:playlistName")
    fun getPlaylistSongs(playlistName: String): Flow<List<Song>>

    @Query("UPDATE songs SET playlist ='' WHERE playlist = :playlistName")
    fun clearPlaylistSongs(playlistName: String)

    @Query("UPDATE songs SET is_favorite = 0 WHERE is_favorite =1")
    fun clearFavoritesSongs()

    @Query("SELECT * FROM songs ORDER BY last_played DESC LIMIT :limit")
    fun getRecentSongs(limit: Int): Flow<List<Song>>

    @Query("SELECT COUNT(id) FROM songs")
    fun rowCount(): Int

    @Query("SELECT COUNT(id) FROM songs WHERE audio_path = :path")
    fun doesSongExist(path: String): Int

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' ")
    fun searchSong(query: String): Flow<List<Song>>

}