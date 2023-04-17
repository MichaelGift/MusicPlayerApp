package com.myth.musicplayerapp.room

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore.Audio.Media

class MusicRepository(private val dao: MusicDao, val context: Context) {

    val deviceMusic = getAllMusicOnDevice()
    val playList = dao.getAllMusicPlaylist()

    suspend fun insertMusic(music: Music) {
        return dao.insertMusic(music)
    }

    suspend fun deleteMusic(music: Music) {
        return dao.deleteMusic(music)
    }

    suspend fun updateMusic(music: Music) {
        return dao.updateMusic(music)
    }

    private fun getAllMusicOnDevice(): List<Music> {
        val musicList = ArrayList<Music>()

        val contentResolver: ContentResolver = context.contentResolver
        val uri = Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            Media._ID,
            Media.TITLE,
            Media.ARTIST,
            Media.ALBUM,
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

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val album = it.getString(albumColumn)
                val contentUri: Uri = ContentUris.withAppendedId(uri, id)

                musicList.add(
                    Music(
                        id,
                        title,
                        album,
                        artist,
                        contentUri
                    )
                )
            }
        }

        return musicList
    }
}