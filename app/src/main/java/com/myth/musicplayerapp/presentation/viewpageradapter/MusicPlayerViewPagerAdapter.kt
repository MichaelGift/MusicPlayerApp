package com.myth.musicplayerapp.presentation.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.myth.musicplayerapp.presentation.fragments.LibraryFragment
import com.myth.musicplayerapp.presentation.fragments.PlayerFragment
import com.myth.musicplayerapp.presentation.fragments.QueueFragment

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