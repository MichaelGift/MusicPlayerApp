package com.myth.musicplayerapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myth.musicplayerapp.room.MusicRepository

class MusicViewModelFactory(private val repository: MusicRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MusicViewModel::class.java)){
            return MusicViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown View Model class")

    }
}