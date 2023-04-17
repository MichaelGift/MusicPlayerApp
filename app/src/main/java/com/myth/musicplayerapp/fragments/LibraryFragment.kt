package com.myth.musicplayerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myth.musicplayerapp.databinding.FragmentLibraryBinding
import com.myth.musicplayerapp.fragments.fragmentadapters.LibraryFragmentAdapter
import com.myth.musicplayerapp.room.Music
import com.myth.musicplayerapp.room.MusicDataBase
import com.myth.musicplayerapp.room.MusicRepository
import com.myth.musicplayerapp.viewmodels.MusicViewModel
import com.myth.musicplayerapp.viewmodels.MusicViewModelFactory

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var repository: MusicRepository
    private lateinit var playerFragment: PlayerFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLibraryBinding.inflate(
            inflater,
            container,
            false
        )
        val dao = MusicDataBase.getInstance(inflater.context).musicDao
        repository = MusicRepository(dao, inflater.context)
        val factory = MusicViewModelFactory(repository)
        playerFragment = PlayerFragment()

        musicViewModel = ViewModelProvider(this, factory)[MusicViewModel::class.java]

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.libraryRecyclerView.layoutManager = LinearLayoutManager(context)
        displayMusicList()
    }

    private fun displayMusicList() {
        binding.libraryRecyclerView.adapter =
            LibraryFragmentAdapter(repository.deviceMusic) { selectedItem: Music ->
                listItemClicked(selectedItem)
            }
    }

    private fun listItemClicked(selectedItem: Music) {
        this.context?.let { playerFragment.playSelectedMusic(selectedItem, it) }
    }
}