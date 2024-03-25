package com.example.playlistmaker2.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.model.Track

class TrackAdapter(
    var trackList: ArrayList<Track>, private var itemClickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener.onTrackClick(track)
        }
    }
    override fun getItemCount(): Int{
        return trackList.size
    }
    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}