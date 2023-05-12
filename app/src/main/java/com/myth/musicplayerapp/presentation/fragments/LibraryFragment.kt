package com.myth.musicplayerapp.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.myth.musicplayerapp.MainActivity
import com.myth.musicplayerapp.databinding.FragmentLibraryBinding
import com.myth.musicplayerapp.presentation.adapter.SongAdapter
import com.myth.musicplayerapp.presentation.viewmodel.SongViewModel
import kotlinx.coroutines.launch

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

        if (!isReadPermissionGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            loadMusicData()
        }
        setUpRecyclerView()
        setDataObservers()
    }

    private fun setUpRecyclerView() {
        songAdapter = SongAdapter(activity as MainActivity)

        binding?.libraryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                (activity as MainActivity)
            )
            adapter = songAdapter
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                loadMusicData()
            } else {
                Toast.makeText(activity, "Access Denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun isReadPermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                (activity as MainActivity).applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun loadMusicData() {
        songViewModel.saveAllSongsToDatabase((activity as MainActivity).applicationContext) {

        }
    }

    private fun setDataObservers() {
        lifecycleScope.launch {
            songViewModel.getAllSongs().collect() { songList ->
                songAdapter.differ.submitList(songList)
            }
        }
    }
}