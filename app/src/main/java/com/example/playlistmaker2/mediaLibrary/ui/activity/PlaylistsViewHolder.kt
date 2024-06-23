package com.example.playlistmaker2.mediaLibrary.ui.activity

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.models.PlaylistToRv

class PlaylistsViewHolder(
    private var binding: PlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlists: PlaylistToRv) {
        binding.apply {
            tvNamePlaylist.text = playlists.playlistName
            countTrack.text = playlists.tracksCount.toString()

            if (playlists.uri != null) {
                playlistImage.setImageURI(playlists.uri.toUri())
            } else {
                playlistImage.setImageResource(R.drawable.ic_stub)
            }
        }
    }
}