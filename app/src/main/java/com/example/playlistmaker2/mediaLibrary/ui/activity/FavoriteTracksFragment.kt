package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker2.databinding.FavoriteTracksFragmentBinding
import com.example.playlistmaker2.mediaLibrary.models.FavoriteState
import com.example.playlistmaker2.mediaLibrary.view_model.FavoriteTracksFragmentViewModel
import com.example.playlistmaker2.player.ui.activity.PlayerDisplayActivity
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.activity.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment(),
    TrackAdapter.TrackClickListener {

    private val viewModel by viewModel<FavoriteTracksFragmentViewModel>()
    private var adapter: TrackAdapter? = null
    private var isClickAllowed = true

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        private const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FavoriteTracksFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(java.util.ArrayList(),this)

        binding.rvFavoriteList.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.fillData()
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            val Intent = Intent(requireContext(), PlayerDisplayActivity::class.java)
            Intent.putExtra(TRACK, track)
            startActivity(Intent)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fillData()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showEmpty()
        }
    }

    private fun showContent(tracks: List<Track>) {
        val sortedTracks = tracks.sortedByDescending { it.addedDate }
        binding.apply {
            rvFavoriteList.isVisible = true
            ivPlaceholderFragment.isVisible = false
            tvPlaceholderMessage.isVisible = false
            rvFavoriteList.adapter = adapter
            adapter?.trackList = sortedTracks
            adapter?.notifyDataSetChanged()
        }
    }

    private fun showEmpty() {
        binding.apply {
            rvFavoriteList.isVisible = false
            ivPlaceholderFragment.isVisible = true
            tvPlaceholderMessage.isVisible = true
        }
    }
}