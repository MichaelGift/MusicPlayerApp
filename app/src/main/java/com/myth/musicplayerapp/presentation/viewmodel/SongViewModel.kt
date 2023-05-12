package com.myth.musicplayerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.myth.musicplayerapp.repository.SongRepository

class SongViewModel(
    private val songRepository: SongRepository
) : ViewModel() {

}