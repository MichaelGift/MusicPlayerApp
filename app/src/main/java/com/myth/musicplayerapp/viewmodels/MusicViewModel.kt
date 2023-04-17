package com.myth.musicplayerapp.viewmodels

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import com.myth.musicplayerapp.room.MusicRepository

class MusicViewModel(private val repository: MusicRepository) : ViewModel(), Observable{

    val deviceMusic = repository.deviceMusic


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

}
