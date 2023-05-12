package com.myth.musicplayerapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myth.musicplayerapp.repository.SongRepository

class SongViewModelFactory(
    val app: Application,
    private val songRepository: SongRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongViewModel(app, songRepository) as T
    }
}