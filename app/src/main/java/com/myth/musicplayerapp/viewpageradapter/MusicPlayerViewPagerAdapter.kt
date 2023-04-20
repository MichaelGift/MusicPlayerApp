package com.myth.musicplayerapp.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.myth.musicplayerapp.fragments.LibraryFragment
import com.myth.musicplayerapp.fragments.PlayerFragment
import com.myth.musicplayerapp.fragments.QueueFragment

class MusicPlayerViewPagerAdapter(fragmentManager : FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return QueueFragment()
            2 -> return LibraryFragment()
            else -> return PlayerFragment()
        }

    }
}