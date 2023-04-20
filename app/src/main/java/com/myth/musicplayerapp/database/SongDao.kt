package com.myth.musicplayerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.myth.musicplayerapp.models.Song

@Dao
interface SongDao {
    @Delete
    suspend fun deleteSong(song: Song)

    @Update
    suspend fun updateSong(song: Song)

    @Query("SELECT * FROM songs ORDER BY songTitle DESC")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM songs WHERE songTitle LIKE '%' || :query || '%' OR songArtist LIKE '%' || :query || '%' ")
    fun searchSong(query: String): LiveData<List<Song>>

}