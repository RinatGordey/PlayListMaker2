package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import com.example.playlistmaker2.mediaLibrary.view_model.CreatePlaylistFragmentViewModel.Companion.MY_IMAGE_PLAYLIST
import java.io.File

class PlaylistsViewHolder(
    private var binding: PlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private var trackCount = 0
    private var caseTrack = ""
    var uri: Uri? = null
    var number: Int = 0

    companion object {
        const val TRACK = "трек"
        const val TRACK_A = "трека"
        const val TRACK_OV = "треков"
    }

    fun bind(playlists: Playlist, context: Context) {
        trackCount = playlists.tracksCount
        binding.apply {
            tvNamePlaylist.text = playlists.playlistName
            while (playlists.tracksCount > 20) {
                trackCount -= 20
            }
            caseTrack = when (trackCount) {
                1 -> {
                    TRACK
                }
                in 1..4 -> {
                    TRACK_A
                }
                else -> {
                    TRACK_OV
                }
            }

            val text = playlists.tracksCount.toString()
            countTrack.text = "$text $caseTrack"

            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_IMAGE_PLAYLIST)
            if (playlists.uri != null) {
                val file = File(filePath, playlists.uri)
                uri = if (file.exists()) Uri.fromFile(file) else null
            } else {
                uri = null
            }

            Glide.with(itemView)
                .load(uri)
                .placeholder(R.drawable.ic_stub2)
                .centerCrop()
                .transform(RoundedCorners(4))
                .into(playlistImage)
        }
    }
}