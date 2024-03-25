package com.example.playlistmaker2.player.ui.mapper

import com.example.playlistmaker2.player.ui.model.TrackInfo
import com.example.playlistmaker2.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = track.collectionName,
            releaseDate = ((track.releaseDate)?:"").substring(0, 4),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl?:"",
        )
    }
}