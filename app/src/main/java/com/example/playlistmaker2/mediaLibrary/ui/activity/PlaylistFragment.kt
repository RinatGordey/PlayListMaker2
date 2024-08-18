package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentPlaylistBinding
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.mediaLibrary.domain.models.PlaylistState
import com.example.playlistmaker2.mediaLibrary.view_model.PlaylistFragmentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment(),
    PlaylistAdapter.PLClickListener {

    companion object {
        fun newInstance() = PlaylistFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var adapter: PlaylistAdapter? = null
    private val viewModel by viewModel<PlaylistFragmentViewModel>()
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistAdapter(requireContext(), this)

        binding.btNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
        }

        binding.rvPlaylist.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvPlaylist.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.getData()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.PlaylistsContent -> showContent(state.playlists)
            is PlaylistState.NoPlaylists -> showEmpty()
        }
    }

    private fun showContent(playlist: List<Playlist>) {
        binding.apply {
            ivPlaceholderFragment.isVisible = false
            tvPlaceholderMessage.isVisible = false
            rvPlaylist.isVisible = true
            adapter?.playlists = playlist as ArrayList<Playlist>
            adapter?.notifyDataSetChanged()
        }
    }

    private fun showEmpty() {
        binding.apply {
            ivPlaceholderFragment.isVisible = true
            tvPlaceholderMessage.isVisible = true
            rvPlaylist.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onPLClick(playlist: Playlist) {
        if (clickDebounce()) {
            isClickAllowed = true
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_currentPlaylistFragment,
                bundleOf(CurrentPlaylistFragment.PLAYLIST to playlist.playlistId)
            )
        }
    }
}