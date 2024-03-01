package com.example.playlistmaker2

import com.example.playlistmaker2.search.domain.models.Track

data class TrackResponse(
    val results: List<Track>
)