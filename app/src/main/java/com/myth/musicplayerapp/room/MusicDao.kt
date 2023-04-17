package com.myth.musicplayerapp.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MusicDao {

    @Insert
    suspend fun insertMusic(music: Music)

    @Update
    suspend fun updateMusic(music: Music)

    @Delete
    suspend fun deleteMusic(music: Music)

    @Query("SELECT * FROM music")
    fun getAllMusicPlaylist(): LiveData<List<Music>>

}