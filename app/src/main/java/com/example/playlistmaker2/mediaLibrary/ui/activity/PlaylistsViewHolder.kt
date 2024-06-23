package com.example.playlistmaker2.mediaLibrary.ui.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlists

class PlaylistsViewHolder(
    private var binding: PlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlists: Playlists) {
        binding.apply {
            tvNamePlaylist.text = playlists.playlistName
            countTrack.text = playlists.count.toString()
            Glide.with(itemView)
                .load(playlists.uri)
                .placeholder(R.drawable.ic_stub)
                .centerCrop()
                .transform(RoundedCorners(4))
                .into(playlistImage)
        }
    }
}