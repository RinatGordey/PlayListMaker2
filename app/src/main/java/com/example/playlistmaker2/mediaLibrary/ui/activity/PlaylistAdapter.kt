package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlists

class PlaylistAdapter(
    private val playlists: List<Playlists>,
): RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}