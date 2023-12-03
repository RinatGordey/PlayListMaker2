package com.example.playlistmaker2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView
    private val artworkUrl100View: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.trackName)
        artistNameView = itemView.findViewById(R.id.artistName)
        trackTimeView = itemView.findViewById(R.id.trackTime)
        artworkUrl100View = itemView.findViewById(R.id.imAlbumsCover)
    }

    fun bind(track: Track) {
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = track.trackTime

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.radius_2dp)
        val roundedCorners = RoundedCorners(radius)
        val requestOption = RequestOptions().transform(CenterCrop(), roundedCorners)

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(requestOption)
            .placeholder(R.drawable.figushka)
            .into(artworkUrl100View)

    }
}