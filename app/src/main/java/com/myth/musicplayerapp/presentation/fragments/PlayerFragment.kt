package com.myth.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.databinding.FragmentPlayerBinding
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PlayerFragment(private val activity: MainActivity) : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private var isTrackSeekingAllowed = true
    private var songJob: Job? = null
    private lateinit var songViewModel: SongViewModel

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
        songViewModel = activity.songViewModel
        val musicService = activity.getMusicService()

        initializeListeners()
        /*if (musicService != null) {
            *//*println("We're past the dark zone boys")
            musicService.setMusicPlaylist(songViewModel.getPlaylist())*//*

        }*/
        initializeData()
    }

    private fun initializeListeners() {

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


        binding.playButton.setOnClickListener {
            val musicService = activity.getMusicService()

            if (musicService != null) {
                println("Current playlist has ${musicService.musicPlaylist.size} songs")

                val isMusicPlaying = musicService.isMusicPlaying()
                if (!isMusicPlaying) {
                    musicService.resumeMusic()
                } else {
                    musicService.pauseMusic()
                }
            } else {
                Toast.makeText(context, "Music Service exists not here", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forwardButton.setOnClickListener {
            val musicService = activity.getMusicService()
            if (musicService != null) {
                if (musicService.forwardMusic()) {
                    initializeData()
                } else {
                    /*checkForRewindAndForward()*/
                    Toast.makeText(context, "Hold on cowboy!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Music Service exists not here", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rewindButton.setOnClickListener {
            val musicService = activity.getMusicService()
            if (musicService != null) {
                if (musicService.rewindMusic()) {
                    initializeData()
                } else {
                    /*checkForRewindAndForward()*/
                    Toast.makeText(context, "Hold on cowboy! Back Up", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Music Service exists not here", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*private fun checkForRewindAndForward() {
        val musicService = activity.getMusicService()
        if (musicService != null) {
            if (musicService.hasFowardedMusic())
        }
    }*/


    private fun initializeData() {
        val musicService = activity.getMusicService()
        songJob?.cancel()

        if (songViewModel.checkSongInit()) {
            binding.songTitle.text = songViewModel.selectedSong.title
            val durationInMillis = musicService!!.getMusicDuration()
            val durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis.toLong())
            val durationInSeconds =
                TimeUnit.MILLISECONDS.toSeconds(durationInMillis.toLong()) - TimeUnit.MINUTES.toSeconds(
                    durationInMinutes
                )
            binding.songLength.text = String.format("%d:%02d", durationInMinutes, durationInSeconds)

            binding.seekBar.max = musicService!!.getMusicDuration() / 1000

            songJob = lifecycleScope.launch(Dispatchers.IO) {
                while (isActive) {
                    if (isTrackSeekingAllowed) {
                        val currentSeek = musicService?.getMusicSeek()
                        withContext(Dispatchers.Main) {
                            binding.seekBar.progress = currentSeek?.div(1000) ?: 0
                            binding.timeLeftText.text = currentSeek?.let { formatMillisToMinutesAndSeconds(it.toLong()) } ?: "0:00"
                        }
                    }
                    delay(1000)
                }
            }
        } else {
            Toast.makeText(activity, "Not yet", Toast.LENGTH_SHORT).show()
        }
    }
    private fun formatMillisToMinutesAndSeconds(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onResume() {
        super.onResume()
        initializeData()
    }
}
