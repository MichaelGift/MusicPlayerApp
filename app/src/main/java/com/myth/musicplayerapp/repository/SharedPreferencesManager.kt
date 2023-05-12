package com.myth.musicplayerapp.repository

import android.content.Context

class SharedPreferencesManager {

    private var openedPlaylistName = ""
    private var selectedTheme: AppTheme? = null
    private val playlistNames = ArrayList<String>()

    fun setOpenedPlaylist(name: String) {
        openedPlaylistName = name
    }

    fun addPlaylist(name: String) {
        playlistNames.add(name)
    }

    fun removePlaylist(name: String) {
        playlistNames.remove(name)
    }

    fun getPlaylists(context: Context): ArrayList<String> {
        if (playlistNames.isEmpty()) playlistNames.addAll(getPlaylistsNames(context).split("&&"))
        return playlistNames
    }

    fun savePlaylists(context: Context) {
        val savedPlaylist =
            context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return
        val edit = savedPlaylist.edit()
        edit.putString("saved_playlists", playlistNames.toString())
        edit.apply()
    }

    private fun getPlaylistsNames(context: Context): String {
        val savedPlaylist =
            context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return ""
        return savedPlaylist.getString("saved_playlist", "")!!
    }

    fun saveTheme(context: Context, theme: AppTheme) {
        val sp = context.getSharedPreferences("App Data", Context.MODE_PRIVATE) ?: return
        val edit = sp.edit()
        edit.putString("app_theme", theme.toString())
        edit.apply()
        selectedTheme = theme
    }

    fun getTheme(context: Context):AppTheme{
        if(selectedTheme != null) return selectedTheme!!
        val sp= context.getSharedPreferences("App Data",Context.MODE_PRIVATE)?: return AppTheme.SHERPA_BLUE
        selectedTheme = sp.getString("app_theme", "SHERPA_BLUE")!!.toAppTheme()
        return selectedTheme!!
    }
}