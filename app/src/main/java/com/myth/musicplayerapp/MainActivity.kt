
package com.myth.musicplayerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
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

    fun generateViewIcon():ArrayList<TabIconData>{
        var icons = ArrayList<TabIconData>()

        var playMusic = TabIconData(R.drawable.play_icon)
        var queueIcon = TabIconData(R.drawable.back_icon)
        var libraryIcon = TabIconData(R.drawable.forward_icon)


        icons.add(queueIcon)
        icons.add(playMusic)
        icons.add(libraryIcon)

        return icons
    }
}
