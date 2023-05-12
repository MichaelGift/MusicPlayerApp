package com.myth.musicplayerapp.repository.utility

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.myth.musicplayerapp.data.models.Song
import java.io.File

class FileManager {
    private lateinit var externalStorageDirectory: File
    private var isFileSystemInitialized = false
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID
    )

    fun getAllAudioFiles(context: Context): ArrayList<Song> {
        var selectionArgs: Array<String>? = null
        val audioList = ArrayList<Song>()
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " + MediaStore.Audio.Media.IS_PENDING + "= 0"
        } else {
            selectionArgs = arrayOf("1")
            MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " + MediaStore.Audio.Media.DATA + "IS NOT NULL"
        }

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        if ((cursor != null) && cursor.moveToFirst()) {
            do {
                val audioPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                if (!File(audioPath).exists()) {
                    continue
                }

                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val albumId =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

                Log.e("--------", "$audioPath | $title | $artist | $duration | $albumId")
                audioList.add(
                    Song(
                        0,
                        title,
                        artist,
                        duration.toLong(),
                        audioPath,
                        albumId.toLong(),
                        false,
                        0,
                        ""
                    )
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
        return audioList
    }

    private fun initializeFileSystem() {
        if (isFileSystemInitialized) return
        externalStorageDirectory = Environment.getExternalStorageDirectory()
    }

    fun listDirectory(path: String) {
        initializeFileSystem()

        if (isFolder(path)) {
            val file = File(externalStorageDirectory, path)
        }
    }

    fun itExists(path: String): Boolean {
        initializeFileSystem()
        val file = File(externalStorageDirectory, path)
        return file.exists()
    }

    fun isFolder(path: String): Boolean {
        initializeFileSystem()
        val file = File(externalStorageDirectory, path)
        return file.isDirectory
    }

    fun deleteFile(path: String) {
        initializeFileSystem()
        if (itExists(path)) {
            val file = File(externalStorageDirectory, path)
            file.delete()
        }
    }

    fun saveFileInternal(context: Context, jsonFileName: String, content: String) {
        context.openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
            it.close()
        }
    }

    fun getFileInternal(context: Context, jsonFileName: String): String {
        context.openFileInput(jsonFileName).use {
            return String(it.readBytes())
        }
    }
}