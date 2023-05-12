package com.myth.musicplayerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myth.musicplayerapp.repository.SongRepository

@Suppress("UNCHECKED_CAST")
class SongViewModelFactory(
    private val songRepository: SongRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongViewModel(songRepository) as T
    }
}