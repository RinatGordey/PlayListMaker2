package com.example.playlistmaker2.search.domain.api

import com.example.playlistmaker2.search.domain.model.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer : TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}