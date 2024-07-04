package com.example.playlistmaker2.player.ui.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.databinding.BottomSheetItemBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlist

class BottomSheetAdapter(
    val playlistClickListener: PlaylistClickListener,
    val context: Context,
) : RecyclerView.Adapter<BottomSheetViewHolder>() {

    var playlists: ArrayList<Playlist> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return BottomSheetViewHolder(BottomSheetItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val item = playlists[position]
        holder.bind(item, context)
        holder.setOnPlaylistClickListener(object : BottomSheetViewHolder.OnPlaylistClickListener {
            override fun action() {
                playlistClickListener.onPlaylistClick(item)
            }
        })
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}