package com.example.playlistmaker2

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(
    private var trackList: MutableList<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackNameView: TextView = itemView.findViewById(R.id.trackName)
        private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
        private val trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
        private val artworkUrl100View: ImageView = itemView.findViewById(R.id.imAlbumsCover)

        fun bind(track: Track) {
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
}
