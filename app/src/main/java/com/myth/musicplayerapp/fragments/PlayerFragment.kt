package com.myth.musicplayerapp.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.myth.musicplayerapp.R
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {

    private var startTime = 0.0
    private var finalTime = 0.0
    private var forwardTime = 10000
    private var rewindTime = 10000
    private var oneTimeOnly = 0

    private var handler : Handler = Handler()
    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var timeText : TextView
    private lateinit var songLength : TextView
    private lateinit var seekBar: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_player, container, false)
        val pauseButton = view.findViewById<Button>(R.id.pause_button)

        val titleText = view.findViewById<TextView>(R.id.song_title)
        timeText = view.findViewById(R.id.time_left_text)
        songLength = view.findViewById(R.id.song_length)
        seekBar = view.findViewById(R.id.seek_bar)

        mediaPlayer = MediaPlayer.create(
            view.context,
            R.raw.take_over
        )

        pauseButton.isVisible = false
        seekBar.isClickable = false

        titleText.text= resources.getResourceEntryName(R.raw.take_over)

        handler.postDelayed(updateSongTime, 100)

        return view
    }
    public fun playMusic(view: View){
        view as Button
        mediaPlayer.start()
        finalTime = mediaPlayer.duration.toDouble()
        startTime = mediaPlayer.currentPosition.toDouble()

        if(oneTimeOnly == 0){
            seekBar.max = finalTime.toInt()
            oneTimeOnly = 1
        }

        timeText.text = startTime.toString()
        songLength.text = buildString {
            append(
                String.format(
                    "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) % 60
                )
            )
        }
        seekBar.progress = startTime.toInt()

        handler.postDelayed(updateSongTime, 100)
        val playButton : Button  = view.findViewById(R.id.play_button)
        playButton.isVisible = false
        val pauseButton : Button = view.findViewById(R.id.pause_button)
        pauseButton.isVisible = true
    }
    public fun pauseMusic(view: View){
        view as Button
        mediaPlayer.pause()

        val pauseButton : Button = view.findViewById(R.id.pause_button)
        pauseButton.isVisible = false

        val playButton : Button = view.findViewById(R.id.play_button)
        playButton.isVisible = true
    }
    public fun forwardMusic(view: View){
        view as Button
        val temp = startTime
        if((temp + forwardTime)<= finalTime){
            startTime += forwardTime
            mediaPlayer.seekTo(startTime.toInt())
        }else{
            Toast.makeText(
                view.context,
                "Cannot Jump Further than Song Length",
                Toast.LENGTH_LONG).show()
        }
    }
    public fun rewindMusic(view : View){
        view as Button
        val temp = startTime.toInt()
        if((temp - rewindTime)>0){
            startTime -= rewindTime
            mediaPlayer.seekTo(startTime.toInt())
        }else{
            Toast.makeText(
                view.context,
                "Cannot Jump to Before Song Started",
                Toast.LENGTH_LONG).show()
        }
    }

    private val updateSongTime : Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            try {
                timeText.text = buildString {
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

            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateSongTime)
    }
}