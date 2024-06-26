package com.example.playlistmaker2.player.ui.activity

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.BottomSheetItemBinding
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import java.io.File

class BottomSheetViewHolder (
    private var binding: BottomSheetItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

    private var trackCount = 0
    private var caseTrack = ""
    private var file: File? = null
    private var uri: Uri? = null

    companion object {
        const val TRACK = "трек"
        const val TRACK_A = "трека"
        const val TRACK_OV = "треков"
        const val PLAYLIST = "playlist"
    }

    fun bind(playlists: Playlist, context: Context) {
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
                val filePath =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLIST)
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
                .into(binding.trackImage)
        }
    }

    fun setOnPlaylistClickListener(listener: onPlaylistClickListener) {
        itemView.setOnClickListener {
            listener.action()
        }
    }

    interface onPlaylistClickListener {
        fun action()
    }
    }
