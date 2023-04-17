package com.myth.musicplayerapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Music::class], version = 1)
abstract class MusicDataBase : RoomDatabase() {
    abstract val musicDao: MusicDao

    companion object {
        @Volatile
        private var INSTANCE: MusicDataBase? = null
        fun getInstance(context: Context): MusicDataBase {
            synchronized(this) {}
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDataBase::class.java,
                    "music_db"
                ).build()
            }
            return instance
        }
    }
}