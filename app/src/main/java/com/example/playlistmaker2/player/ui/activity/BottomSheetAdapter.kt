package com.example.playlistmaker2.player.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.databinding.BottomSheetItemBinding
import com.example.playlistmaker2.mediaLibrary.models.PlaylistToRv

class BottomSheetAdapter : RecyclerView.Adapter<BottomSheetViewHolder>() {

    lateinit var playlists: ArrayList<PlaylistToRv>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return BottomSheetViewHolder(BottomSheetItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}