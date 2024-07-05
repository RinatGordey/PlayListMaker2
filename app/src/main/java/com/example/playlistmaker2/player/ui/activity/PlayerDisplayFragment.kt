package com.example.playlistmaker2.player.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentPlayerDisplayBinding
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.mediaLibrary.domain.models.PlaylistState
import com.example.playlistmaker2.player.ui.mapper.TrackMapper
import com.example.playlistmaker2.player.ui.model.TrackInfo
import com.example.playlistmaker2.player.ui.view_model.PlayerDisplayViewModel
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.loadTrackPicture
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerDisplayFragment : Fragment(), BottomSheetAdapter.PlaylistClickListener {

    companion object {
        const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }

    private var _binding: FragmentPlayerDisplayBinding? = null
    private val binding get() = _binding!!

    private lateinit var lastTrack: TrackInfo
    private lateinit var adapter: BottomSheetAdapter
    private lateinit var trackForPlaylist: Track
    private var isClickAllowed = true

    private val viewModel: PlayerDisplayViewModel by viewModel {
        parametersOf(lastTrack)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentPlayerDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(this, requireContext())
        binding.rvBottomSheet.adapter = adapter

        binding.rvBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        adapter.notifyDataSetChanged()

        trackForPlaylist = requireArguments().getSerializable(TRACK) as Track
        lastTrack = TrackMapper().map(trackForPlaylist)

        arrowBack(binding)

        viewModel.create()

        binding.playButton.isEnabled = true

        binding.playButton.setOnClickListener {
            viewModel.play()
        }

        binding.likeButton.setOnClickListener {
            viewModel.likeClick()
        }

        binding.addTrackButton.setOnClickListener {
            viewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        setInfo(binding, lastTrack)

        viewModel.getBottomSheetLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        binding.btNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerDisplayFragment_to_createPlaylistFragment)
        }

        viewModel.getPlayingLiveData().observe(viewLifecycleOwner) { playbackState ->
            if (playbackState.playes) {
                binding.playButton.setImageResource(R.drawable.ic_bt_pause)
            } else {
                binding.playButton.setImageResource(R.drawable.ic_play_bt)
            }
            binding.time.text = playbackState.position
        }

        viewModel.getFavoriteLiveData().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.likeButton.setImageResource(R.drawable.ic_liked)
                binding.likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ic_liked))
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_like_bt)
                binding.likeButton.setColorFilter(Color.WHITE)
            }
        }

        viewModel.getBottomSheetLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.getAddToPlaylistLiveData().observe(viewLifecycleOwner) { action ->
            when (action.first){
                true->{ Toast.makeText(
                    requireContext(), "Трек уже добавлен в плейлист ${action.second}",
                    Toast.LENGTH_LONG
                ).show()
                }
                false->{ Toast.makeText(
                    requireContext(),"Добавлено в плейлист ${action.second}",
                    Toast.LENGTH_LONG
                ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    private fun arrowBack(binding: FragmentPlayerDisplayBinding) {
        binding.btBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setInfo(binding: FragmentPlayerDisplayBinding, lastTrack: TrackInfo) {
        with(binding) {
            nameOfTrack.text = lastTrack.trackName
            authorOfTrack.text = lastTrack.artistName
            durationValue.text = lastTrack.trackTime
            yearValue.text = lastTrack.releaseDate
            genreValue.text = lastTrack.primaryGenreName
            countryValue.text = lastTrack.country
            if (lastTrack.collectionName?.isEmpty() == true) {
                albumValue.isVisible = false
            } else {
                albumValue.isVisible = true
                albumValue.text = lastTrack.collectionName
            }
            val artworkUrl = lastTrack.artworkUrl100
            trackPicture.loadTrackPicture(artworkUrl)
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.onDestroy()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.PlaylistsContent -> showContent(state.playlists)
            is PlaylistState.NoPlaylists -> showEmpty()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.rvBottomSheet.isVisible = true
        binding.rvBottomSheet.adapter = adapter
        adapter.playlists = playlists as ArrayList<Playlist>
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.rvBottomSheet.isVisible = false
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

    override fun onPlaylistClick(playlist: Playlist) {
        if (clickDebounce()) {
            viewModel.addToPlaylist(playlist, trackForPlaylist)
        }
    }
}