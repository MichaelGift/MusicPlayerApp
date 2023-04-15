
package com.myth.musicplayerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myth.musicplayerapp.adapter.MusicPlayerAdapter
import com.myth.musicplayerapp.models.TabIconData

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout : TabLayout = findViewById(R.id.tabViewNav)
        val viewPager : ViewPager2 = findViewById(R.id.viewPager)

        val musicViewAdapter = MusicPlayerAdapter(
            supportFragmentManager,
            lifecycle
        )

        val iconSet = generateViewIcon()
        viewPager.adapter = musicViewAdapter
        TabLayoutMediator(tabLayout, viewPager){
            tab,position ->
            tab.icon = ContextCompat.getDrawable(this, iconSet[position].activeTabIcon)
        }.attach()
    }

    private fun generateViewIcon():ArrayList<TabIconData>{
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
