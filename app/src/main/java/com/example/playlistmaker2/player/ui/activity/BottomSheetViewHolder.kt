package com.example.playlistmaker2.player.ui.activity

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.BottomSheetItemBinding
import com.example.playlistmaker2.mediaLibrary.models.PlaylistToRv

class BottomSheetViewHolder (
    private var binding: BottomSheetItemBinding,
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
            playlistName.text = playlists.playlistName
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
                trackImage.setImageURI(playlists.uri.toUri())
            } else {
                trackImage.setImageResource(R.drawable.ic_stub)
            }
        }
    }
}