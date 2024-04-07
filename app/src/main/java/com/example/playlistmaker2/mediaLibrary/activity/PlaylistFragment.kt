package com.example.playlistmaker2.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker2.databinding.PlaylistFragmentBinding
import com.example.playlistmaker2.mediaLibrary.view_model.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }
    private lateinit var binding: PlaylistFragmentBinding

    private val viewModel by viewModel<PlaylistFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}