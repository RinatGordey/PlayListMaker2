package com.example.playlistmaker2

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale
class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView = itemView.findViewById(R.id.trackName)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    private val artworkUrl100View: ImageView = itemView.findViewById(R.id.imAlbumsCover)
    private val trackId: TextView = itemView.findViewById(R.id.trackId)

    fun bind(track: Track) {
        trackId.text = track.trackId.toString()
        trackNameView.text = track.trackName ?: "Unknown Track"
        artistNameView.text = track.artistName ?: "Unknown Artist"
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale // перевод продолжительности треков в формат 00м:00с
            .getDefault())
            .format(track.trackTime.toLong())

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter().dontAnimate()
            .placeholder(R.drawable.ic_stub)
            .transform(RoundedCorners(dpToPx(8F, itemView.context)))
            .into(artworkUrl100View)
    }
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
            ,dp
            ,context.resources.displayMetrics).toInt()
    }
}