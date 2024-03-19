package com.example.playlistmaker2.search.ui.models

import com.example.playlistmaker2.search.domain.model.Track

sealed interface TrackSearchState {

    data object Loading : TrackSearchState

    data class SearchContent(
        val trackInfo: MutableList<Track>
    ) : TrackSearchState

    data class HistoryContent(
        val trackInfo: MutableList<Track>,
        val isEmpty: Boolean
    ) : TrackSearchState

    data class Error(
        val errorMessage: String
    ) : TrackSearchState

    data class Empty(
        val message: String
    ) : TrackSearchState
}