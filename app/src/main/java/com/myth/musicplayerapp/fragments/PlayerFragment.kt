package com.myth.musicplayerapp.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.myth.musicplayerapp.R
import com.myth.musicplayerapp.databinding.FragmentPlayerBinding
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class PlayerFragment : Fragment() {

    var isPlaying: Boolean = false

    private var startTime = 0.0
    private var finalTime = 0.0
    private var forwardTime = 10000
    private var rewindTime = 10000
    private var oneTimeOnly = 0

    private var handler: Handler = Handler()
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var binding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlayerBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {

            playButton.setOnClickListener {
                playMusic()
            }

            pauseButton.setOnClickListener {
                pauseMusic()
            }

            rewindButton.setOnClickListener {
                rewindMusic()
            }

            forwardButton.setOnClickListener {
                forwardMusic()
            }

            pauseButton.isVisible = false
            seekBar.isClickable = false

            mediaPlayer = MediaPlayer.create(
                context,
                R.raw.take_over
            )

            textView.text = resources.getResourceEntryName(R.raw.take_over)
        }

        handler.postDelayed(updateSongTime, 100)

        return binding.root
    }


    private fun playMusic() {

        mediaPlayer.start()

        finalTime = mediaPlayer.duration.toDouble()
        startTime = mediaPlayer.currentPosition.toDouble()

        if (oneTimeOnly == 0) {

            binding.seekBar.max = finalTime.toInt()
            oneTimeOnly = 1
        }

        binding.timeLeftText.text = startTime.toString()

        binding.songLength.text = buildString {
            append(
                String.format(
                    "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) % 60
                )
            )
        }

        binding.seekBar.progress = startTime.toInt()

        handler.postDelayed(updateSongTime, 100)

        isPlaying = true
    }

    private fun pauseMusic() {

        mediaPlayer.pause()

        isPlaying = false
    }

    private fun forwardMusic() {

        val temp = startTime

        if ((temp + forwardTime) <= finalTime) {

            startTime += forwardTime
            mediaPlayer.seekTo(startTime.toInt())

        } else {

            Toast.makeText(
                context,
                "Cannot Jump Further than Song Length",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun rewindMusic() {


        val temp = startTime.toInt()

        if ((temp - rewindTime) > 0) {

            startTime -= rewindTime
            mediaPlayer.seekTo(startTime.toInt())

        } else {

            Toast.makeText(
                context,
                "Cannot Jump to Before Song Started",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private val updateSongTime: Runnable = object : Runnable {

        override fun run() {

            startTime = mediaPlayer.currentPosition.toDouble()

            try {
                binding.timeLeftText.text = buildString {
                    append(
                        String.format(
                            "%d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                            TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) % 60
                        )
                    )
                }

            } catch (e: Exception) {

                Log.e("MainActivity", "Error updating timer text: ${e.message}")
            }

            binding.apply {

                seekBar.progress = startTime.toInt()

                if (!isPlaying) {

                    playButton.isVisible = true
                    pauseButton.isVisible = false
                } else {

                    playButton.isVisible = false
                    pauseButton.isVisible = true
                }
            }

            handler.postDelayed(this, 100)
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        mediaPlayer.release()
        handler.removeCallbacks(updateSongTime)
    }
}