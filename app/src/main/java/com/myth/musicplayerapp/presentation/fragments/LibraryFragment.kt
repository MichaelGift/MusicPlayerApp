package com.myth.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.presentation.adapter.SongAdapter
import com.myth.musicplayerapp.databinding.FragmentLibraryBinding
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding

    private lateinit var songViewModel: SongViewModel
    private lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel = (activity as MainActivity).songViewModel
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        songAdapter = SongAdapter(activity as MainActivity)

        binding?.libraryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                (activity as MainActivity)
            )
            adapter = songAdapter
        }



        songViewModel.getAllSongsOnDevice()
        /*songViewModel.getAllSongs()*/
        println("You have ${songViewModel.songsOnDevice.value?.size} songs")
        activity?.let {
            songViewModel.songsOnDevice.observe(
                viewLifecycleOwner
            ) { song ->
                songAdapter.differ.submitList(song)
            }
        }
    }
}