package com.example.playlistmaker2.search.domain.api

import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource

interface TracksSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}