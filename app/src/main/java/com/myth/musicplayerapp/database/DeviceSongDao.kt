package com.myth.musicplayerapp.database

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myth.musicplayerapp.models.Song

class DeviceSongDao {

    val songLiveData = MutableLiveData<List<Song>>()

    fun getAllMusicOnDevice(context: Context): LiveData<List<Song>> {

        val musicList = ArrayList<Song>()

        val contentResolver: ContentResolver = context.contentResolver
        val uri = Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            Media._ID,
            Media.TITLE,
            Media.ARTIST,
            Media.ALBUM,
            Media.DURATION
        )
        val sortOrder = "${Media.TITLE} ASC"

        val cursor: Cursor? = contentResolver.query(
            uri,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(Media.ALBUM)
            val durationColum =it.getColumnIndexOrThrow(Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val album = it.getString(albumColumn)
                val contentUri: Uri = ContentUris.withAppendedId(uri, id)
                val duration = it.getDouble(durationColum)

                musicList.add(
                    Song(
                        id,
                        title,
                        album,
                        artist,
                        contentUri,
                        duration
                    )
                )
            }
        }

        songLiveData.value = musicList
        return songLiveData
    }
}