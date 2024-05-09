package com.example.playlistmaker2.search.domain.api

import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksSearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}