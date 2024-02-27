package com.example.playlistmaker2.glide

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.ConversionDpToPx
import com.example.playlistmaker2.R
import com.example.playlistmaker2.Track

class GlideCreator {
    fun setTrackPicture(trackPicture: ImageView, lastTrack: Track) {
        Glide.with(trackPicture)
            .load(lastTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.ic_stub)
            .transform(RoundedCorners(ConversionDpToPx.dpToPx(8F, trackPicture.context)))
            .into(trackPicture)
    }
}