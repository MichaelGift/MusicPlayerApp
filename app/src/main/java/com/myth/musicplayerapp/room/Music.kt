package com.myth.musicplayerapp.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity("music")
@TypeConverters(UriTypeConverter::class)
class Music(
    @PrimaryKey
    var id: Long,
    var title: String,
    var album: String,
    var artist: String,
    var contentUri : Uri
)