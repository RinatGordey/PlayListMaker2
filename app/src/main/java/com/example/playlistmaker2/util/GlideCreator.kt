package com.example.playlistmaker2.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R

fun ImageView.loadTrackPicture(artworkUrl: String) {
    Glide.with(this)
        .load(artworkUrl.replaceAfterLast('/', "512x512bb.jpg"))
        .placeholder(R.drawable.ic_stub)
        .transform(RoundedCorners(ConversionDpToPx.dpToPx(8F, this.context)))
        .into(this)
}