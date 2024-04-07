package com.example.playlistmaker2.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker2.databinding.FavoriteTracksFragmentBinding

class FavoriteTracksFragment: Fragment() {

    companion object {

        fun newInstance() = FavoriteTracksFragment()
    }

    private lateinit var binding: FavoriteTracksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
