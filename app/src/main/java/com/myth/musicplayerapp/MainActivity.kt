package com.myth.musicplayerapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.myth.musicplayerapp.data.database.SongDatabase
import com.myth.musicplayerapp.presentation.viewpageradapter.MusicPlayerViewPagerAdapter
import com.myth.musicplayerapp.databinding.ActivityMainBinding
import com.myth.musicplayerapp.data.models.TabIconData
import com.myth.musicplayerapp.repository.SongRepository
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModelFactory

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var songViewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val musicViewAdapter = MusicPlayerViewPagerAdapter(
            supportFragmentManager,
            lifecycle
        )

        binding.apply {

            viewPager.adapter = musicViewAdapter
            val iconSet = generateViewIcon()

            TabLayoutMediator(tabViewNav, viewPager,) { tab, position ->
                tab.icon =
                    ContextCompat.getDrawable(this@MainActivity, iconSet[position].activeTabIcon)
            }.attach()

            tabViewNav.getTabAt(1)?.select()
        }
        setUpViewModel()
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
    fun setUpViewModel(){
        val songRepository = SongRepository(SongDatabase(this),this.application, DeviceSongDao())
        val viewModelProviderFactory = SongViewModelFactory(application, songRepository)
        songViewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[SongViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onDestroy() {
        Toast.makeText(applicationContext, "Destroyed", Toast.LENGTH_SHORT).show()
        super.onDestroy()
        songViewModel.mediaPlayer?.release()
    }
}
