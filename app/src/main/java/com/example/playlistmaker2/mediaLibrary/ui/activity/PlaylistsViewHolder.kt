package com.example.playlistmaker2.mediaLibrary.ui.activity

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.models.PlaylistToRv

class PlaylistsViewHolder(
    private var binding: PlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private var trackCount = 0
    private var caseTrack = ""

    companion object {
        const val TRACK = "трек"
        const val TRACK_A = "трека"
        const val TRACK_OV = "треков"
    }

    fun bind(playlists: PlaylistToRv) {
        binding.apply {
            tvNamePlaylist.text = playlists.playlistName
            while (playlists.tracksCount > 20) {
                trackCount -= 20
            }
            caseTrack = if (trackCount == 1) {
                TRACK
            } else if (trackCount in 1..4) {
                TRACK_A
            } else {
                TRACK_OV
            }

            val text = playlists.tracksCount.toString()
            binding.countTrack.text = "$text $caseTrack"

            if (playlists.uri != null) {
                playlistImage.setImageURI(playlists.uri.toUri())
            } else {
                playlistImage.setImageResource(R.drawable.ic_stub)
            }
        }
    }
}