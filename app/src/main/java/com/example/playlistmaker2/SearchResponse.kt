package com.example.playlistmaker2

data class SearchResponse(
    val resultCount: Int,
    val result: List<Track>
)
