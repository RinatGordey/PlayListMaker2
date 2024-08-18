package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist

class PlaylistAdapter(
    val context: Context,
    val plClickListener: PLClickListener,
): RecyclerView.Adapter<PlaylistsViewHolder>() {

    lateinit var playlists: ArrayList<Playlist>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val item = playlists[position]
        holder.bind(playlists[position], context)

        holder.setOnCPLListener(object : onCPLClickListener {
            override fun action() {
                plClickListener.onPLClick(item)
            }
        })
    }

    fun interface PLClickListener {
        fun onPLClick(playlist: Playlist)
    }
}