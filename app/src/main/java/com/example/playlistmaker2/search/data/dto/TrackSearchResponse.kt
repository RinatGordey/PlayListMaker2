package com.example.playlistmaker2.search.data.dto

import com.example.playlistmaker2.search.domain.models.Track

data class TrackSearchResponse(
    val resultCount: Int,
    val result: List<Track>
)
