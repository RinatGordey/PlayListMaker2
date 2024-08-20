package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCurrentPlaylistBinding
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.mediaLibrary.domain.models.TrackListState
import com.example.playlistmaker2.mediaLibrary.view_model.CreatePlaylistFragmentViewModel.Companion.MY_IMAGE_PLAYLIST
import com.example.playlistmaker2.mediaLibrary.view_model.CurrentPlaylistViewModel
import com.example.playlistmaker2.player.ui.activity.PlayerDisplayFragment
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.activity.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class CurrentPlaylistFragment : Fragment(), TrackAdapter.TrackClickListener,
    TrackAdapter.LongClickListener {

    private var pl: Playlist? = null
    private var adapter: TrackAdapter? = null
    private var isClickAllowed = true
    private var id: Long = 0
    private val viewModel: CurrentPlaylistViewModel by viewModel { parametersOf(id) }
    private var _binding: FragmentCurrentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var callback: OnBackPressedCallback? = null
    private var tracks: List<Track>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentCurrentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = requireArguments().getLong(PLAYLIST)

        viewModel.getCurrentPLTracksLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is TrackListState.TrackListContent -> {
                    adapter = TrackAdapter(state.tracks, this, this)
                    binding.bottomSheetRecycler.adapter = adapter
                }

                is TrackListState.NoContent -> {}
            }
        }

        binding.bottomSheetRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val bottomSheetMenu = BottomSheetBehavior.from(binding.bottomSheetMenu)

        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN

        binding.imBtMenu.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.playlistName.text = pl?.playlistName
            binding.tvDescription.text = pl?.playlistDescription
            val count = pl?.tracksCount
            val trackPlural =
                count?.let { it1 ->
                    this.resources.getQuantityString(
                        R.plurals.plurals_2,
                        it1,
                        count
                    )
                }
            binding.countTrack.text = trackPlural

            val uri = pl?.let { it1 -> getImage(it1) }
            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.ic_stub2)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(binding.trackCover)
        }

        binding.sharePlaylists.setOnClickListener {
            share()
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.imBtShare.setOnClickListener {
            share()
        }

        binding.editInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_currentPlaylistFragment_to_editPlaylistFragment,
                pl?.let { it1 -> EditPlaylistFragment.createArgs(it1) }
            )
        }

        binding.deletePlaylist.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.customDialog)
                .setTitle(getString(R.string.Do_you_want_to_delete_a_playlist, pl?.playlistName))
                .setNeutralButton(getString(R.string.NO)) { _, _ ->
                }
                .setPositiveButton(getString(R.string.YES)) { _, _ ->
                    pl?.let { it1 -> viewModel.deletePlaylist(it1) }
                    findNavController().navigateUp()
                }
                .create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(resources.getColor(R.color.main_blue))
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.main_blue))
            }
            dialog.show()
        }

        bottomSheetMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback as OnBackPressedCallback)

        binding.btBackImage.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getCurrentPLTracksLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylist(id)
    }

    private fun render(state: TrackListState) {
        when (state) {
            is TrackListState.TrackListContent -> {
                tracks = state.tracks
                pl = state.playlist
                showContent(
                    state.tracks,
                    state.playlist,
                    state.totalTime,
                )
            }

            is TrackListState.NoContent -> {
                pl = state.playlist
                Toast.makeText(requireContext(), TOAST_NO_TRACKS, Toast.LENGTH_SHORT).show()
                showEmpty(state.playlist, state.totalTime)
            }
        }
    }

    private fun showEmpty(playlist: Playlist, time: String) {
        binding.bottomSheet.isVisible = false
        val uri = getImage(playlist)

        if (uri != null) {
            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.ic_stub2)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(binding.coverImageView)
        }

        binding.tvPlaylistName.text = playlist.playlistName
        binding.tvDescription.text = playlist.playlistDescription

        var minutePlural: String
        if (time.isEmpty()) {
            minutePlural = getString(R.string.unknown_duration)
        } else {
            try {
                val parts = time.split(":")

                if (parts.size < 2) {
                    minutePlural = getString(R.string.unknown_duration)
                } else {
                    val minutes = Integer.parseInt(parts[0])
                    val seconds = Integer.parseInt(parts[1])

                    val totalMinutes = minutes + seconds / 60
                    minutePlural = this.resources.getQuantityString(R.plurals.plurals_3, totalMinutes, totalMinutes)
                }
            } catch (e: NumberFormatException) {
                minutePlural = getString(R.string.unknown_duration)
            }
        }

       binding.tvPlaylistDuration.text = minutePlural

        val count = playlist.tracksCount
        val trackPlural = this.resources.getQuantityString(R.plurals.plurals_2, count, count)
        if (count > 0) {
            binding.tvTrackCount.text = trackPlural
        } else {
            binding.ic.isVisible = false
            binding.tvPlaylistDuration.text = getString(R.string.no_tracks)
            binding.tvTrackCount.text = ""
        }
    }

    private fun showContent(tracks: List<Track>, playlist: Playlist, time: String) {
        showEmpty(playlist, time)
        binding.bottomSheet.isVisible = true
        binding.bottomSheetRecycler.adapter = adapter
        if (tracks.size > 1) {
            adapter?.trackList = tracks.reversed() as ArrayList<Track>
        } else {
            adapter?.trackList = tracks as ArrayList<Track>
        }
        adapter?.notifyDataSetChanged()

    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            isClickAllowed = true
            findNavController().navigate(
                R.id.action_currentPlaylistFragment_to_playerDisplayFragment,
                PlayerDisplayFragment.createArgs(track)
            )
        }
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

    private var isDialogOpen = false

    override fun onLongClick(track: Track) {
        showDialog(track)
    }

    private fun showDialog(track: Track) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.customDialog)
            .setTitle(R.string.delete_track)
            .setMessage(R.string.are_you_sure_you_want_to_remove_the_track)
            .setNeutralButton(R.string.cancel) { _, _ ->
                isDialogOpen = false
            }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteTrack(pl?.playlistId!!, track.trackId)
                isDialogOpen = false
            }
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.setOnShowListener {
            isDialogOpen = true
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.main_blue))
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(resources.getColor(R.color.main_blue))
        }

        dialog.setOnDismissListener {
            isDialogOpen = false
        }

        dialog.show()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isDialogOpen) {
                    dialog.dismiss()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    private fun getImage(playlist: Playlist): Uri? {
        if (playlist.uri != null) {
            val filePath = File(requireContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , MY_IMAGE_PLAYLIST)
            val file = playlist.uri?.let { File(filePath, it) }
            if (file != null) {
                return if (file.exists()) file.toUri() else null
            }
        }
        return null
    }

    private fun share() {
        if (pl?.tracksId == "") {
            Toast.makeText(requireContext(), R.string.no_tracks, Toast.LENGTH_SHORT).show()
        } else {
            tracks?.let { pl?.let { it1 -> viewModel.share(it1, it) } }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback?.remove()
        _binding = null
    }

    companion object {
        const val PLAYLIST = "PLAYLIST"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val TOAST_NO_TRACKS = "В этом плейлисте нет треков"
    }
}