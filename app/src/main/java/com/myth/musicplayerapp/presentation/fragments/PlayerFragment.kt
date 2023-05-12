package com.myth.musicplayerapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.databinding.FragmentPlayerBinding
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel
import com.myth.musicplayerapp.repository.service.MusicPlaybackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private var isTrackSeekingAllowed = true
    private var songJob: Job? = null
    private var musicService: MusicPlaybackService? = null
    private lateinit var songViewModel: SongViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            musicService = context.getMusicService()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlayerBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel = (activity as MainActivity).songViewModel
        initializeListeners()
        if (musicService != null) {
            musicService!!.playNewMusic(songViewModel.selectedSong)
            musicService!!.setMusicPlaylist(songViewModel.getPlaylist())
            initializeData()
        }
    }

    fun initializeListeners() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.timeLeftText.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isTrackSeekingAllowed = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isTrackSeekingAllowed = true
                /*musicService!!.setMusicSeek(seekBar!!.progress * 1000)*/
            }
        })
    }

    fun initializeData() {
        songJob?.cancel()

        binding.songLength.text = songViewModel.selectedSong.title
        binding.songLength.text = (musicService!!.getMusicDuration() / 1000).toString()
        binding.seekBar.max = musicService!!.getMusicDuration() / 1000

        songJob = lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                if (isTrackSeekingAllowed) {
                    binding.seekBar.progress = musicService!!.getMusicSeek() / 1000
                } else {
                    delay(1000)
                }
            }
        }
    }
}
