package com.myth.musicplayerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.myth.musicplayerapp.adapter.MusicPlayerAdapter
import com.myth.musicplayerapp.databinding.ActivityMainBinding
import com.myth.musicplayerapp.models.TabIconData

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val musicViewAdapter = MusicPlayerAdapter(
            supportFragmentManager,
            lifecycle
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
}
