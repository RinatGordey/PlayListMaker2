package com.example.playlistmaker2.search.data.dto

class TrackResponse(val resultCount: Int,
                    val results: List<TrackDto>): Response() {
}