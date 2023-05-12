package com.myth.musicplayerapp

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.myth.musicplayerapp.data.database.SongDatabase
import com.myth.musicplayerapp.data.models.TabIconData
import com.myth.musicplayerapp.databinding.ActivityMainBinding
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModelFactory
import com.myth.musicplayerapp.presentation.viewpageradapter.MusicPlayerViewPagerAdapter
import com.myth.musicplayerapp.repository.SongRepository
import com.myth.musicplayerapp.repository.service.MusicPlaybackService

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var musicService: MusicPlaybackService? = null
    private lateinit var binding: ActivityMainBinding

    lateinit var songViewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val intent = Intent(this, MusicPlaybackService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        initialiseListeners()

        val musicViewAdapter = MusicPlayerViewPagerAdapter(
            supportFragmentManager, lifecycle
        )

        binding.apply {

            viewPager.adapter = musicViewAdapter
            val iconSet = generateViewIcon()

            TabLayoutMediator(tabViewNav, viewPager) { tab, position ->
                tab.icon =
                    ContextCompat.getDrawable(this@MainActivity, iconSet[position].activeTabIcon)
            }.attach()

            tabViewNav.getTabAt(1)?.select()
        }
        setUpViewModel()
    }

    fun initialiseListeners() {

    }

    fun stopMusicService() {
        val intent = Intent(this, MusicPlaybackService::class.java)
        stopService(intent)
    }

    fun getMusicService(): MusicPlaybackService? {
        return musicService
    }

    private val musicChangeStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }
    private val musicPlayPauseStateReceiver: BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            
        }
    }

    private fun generateViewIcon(): ArrayList<TabIconData> {
        val icons = ArrayList<TabIconData>()

        val playMusic = TabIconData(R.drawable.musicplayicon)
        val queueIcon = TabIconData(R.drawable.musicqueueicon)
        val libraryIcon = TabIconData(R.drawable.musiclibraryicon)


        icons.add(queueIcon)
        icons.add(playMusic)
        icons.add(libraryIcon)

        return icons
    }

    fun setUpViewModel() {
        val songRepository = SongRepository(SongDatabase(this))
        val viewModelProviderFactory = SongViewModelFactory(songRepository)
        songViewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[SongViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu, menu)
        return true
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: MusicPlaybackService.MyBinder = service as MusicPlaybackService.MyBinder
            musicService = binder.getService()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

    }

    private fun startBroadCasReceiver() {
        val filter1 = IntentFilter()
        filter1.addAction("android.intent.action.MUSIC_STATE_CHANGE")
        registerReceiver(musicChangeStateReceiver, filter1)

        val filter2 = IntentFilter()
        filter2.addAction("android.intent.action.MUSIC_PLAY_PAUSE_STATE_CHANGE")
        registerReceiver(musicPlayPauseStateReceiver, filter2)
    }
    private fun stopBroadCastReceiver(){
        unregisterReceiver(musicChangeStateReceiver)
        unregisterReceiver(musicPlayPauseStateReceiver)

    }

    override fun onDestroy() {
        Toast.makeText(applicationContext, "Destroyed", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}
