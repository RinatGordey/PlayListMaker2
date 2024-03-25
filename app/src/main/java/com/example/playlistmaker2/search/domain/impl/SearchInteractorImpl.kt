package com.example.playlistmaker2.search.domain.impl

import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.api.TracksSearchRepository
import com.example.playlistmaker2.util.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: TracksSearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute {
            // consumer.consume(repository.searchTracks(expression))


            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}