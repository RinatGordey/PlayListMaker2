package com.example.playlistmaker2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView = itemView.findViewById(R.id.trackName)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    private val artworkUrl100View: ImageView = itemView.findViewById(R.id.imAlbumsCover)
    private val trackId: TextView = itemView.findViewById(R.id.trackId)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun bind(track: Track) {
        trackId.text = track.trackId.toString()
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = dateFormat.format(track.trackTime.toLong()) // перевод продолжительности треков в формат 00м:00с

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter().dontAnimate()
            .placeholder(R.drawable.ic_stub)
            .transform(RoundedCorners(ConversionDpToPx.dpToPx(8F, itemView.context)))
            .into(artworkUrl100View)
    }
}