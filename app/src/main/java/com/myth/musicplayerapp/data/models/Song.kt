package com.myth.musicplayerapp.data.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.myth.musicplayerapp.database.UriTypeConverter

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "artist") var artist: String,
    @ColumnInfo(name = "duration") var duration: Long,
    @ColumnInfo(name = "audio_path") var audioPath: String,
    @ColumnInfo(name = "album_id") var albumId: Long,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean,
    @ColumnInfo(name = "last_played") var lastPlayed: Long,
    @ColumnInfo(name = "playlist") var playlist: String
)
