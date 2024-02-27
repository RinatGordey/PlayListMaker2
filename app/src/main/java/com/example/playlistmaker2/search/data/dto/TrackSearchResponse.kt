package com.example.playlistmaker2.search.data.dto

import com.example.playlistmaker2.Track

data class TrackSearchResponse(
    val resultCount: Int,
    val result: List<Track>
)
