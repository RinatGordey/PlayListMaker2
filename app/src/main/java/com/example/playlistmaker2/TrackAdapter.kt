package com.example.playlistmaker2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
class TrackAdapter(
    private var trackList: ArrayList<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((Int, Track) -> Unit)? = null

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
            itemClickListener?.invoke(position, track)
        }
    }
    override fun getItemCount(): Int{
        return trackList.size
    }
}