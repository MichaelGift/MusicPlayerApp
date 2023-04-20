package com.myth.musicplayerapp.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.myth.musicplayerapp.database.UriTypeConverter

@Entity(tableName = "songs")
@TypeConverters(UriTypeConverter::class)
data class Song(
    @PrimaryKey
    val id: Long,
    val songTitle: String,
    val songAlbum: String,
    val songArtist: String,
    val contentUri: Uri,
    val songDuration: Double
)
