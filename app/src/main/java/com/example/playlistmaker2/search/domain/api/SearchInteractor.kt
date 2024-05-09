package com.example.playlistmaker2.search.domain.api

import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}