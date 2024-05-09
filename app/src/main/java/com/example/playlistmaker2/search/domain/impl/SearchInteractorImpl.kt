package com.example.playlistmaker2.search.domain.impl

import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.api.TracksSearchRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: TracksSearchRepository) : SearchInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Pair(resource.data, null)
                }
                is Resource.Error -> {
                    Pair(null, resource.message)
                }
            }
        }
    }
}