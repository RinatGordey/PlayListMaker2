package com.example.playlistmaker2.mediaLibrary.ui.activity

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import java.io.File


class PlaylistsViewHolder(
    private var binding: PlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private var trackCount = 0
    private var caseTrack = ""
    private var file: File? = null
    var uri: Uri? = null
    var number: Int = 0

    companion object {
        const val TRACK = "трек"
        const val TRACK_A = "трека"
        const val TRACK_OV = "треков"
        const val PLAYLISTS = "PL"
    }

    fun bind(playlists: Playlist, context: Context) {
        trackCount = playlists.tracksCount
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
            countTrack.text = "$text $caseTrack"

            if (playlists.uri != null) {
                val filePath =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLISTS)
                file = File(filePath, playlists.uri)

                uri = file!!.toUri()
            } else {
                uri = null
            }


            Glide.with(itemView)
                .load(uri)
                .placeholder(R.drawable.ic_stub)
                .centerCrop()
                .transform(RoundedCorners(4))
                .into(playlistImage)
        }
    }
}