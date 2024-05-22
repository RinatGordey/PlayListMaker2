package com.example.playlistmaker2.db.presentation

import com.example.playlistmaker2.search.domain.model.Track

sealed interface FavoriteState {

    data object Loading: FavoriteState

    data class Content(
        val tracks: List<Track>
    ): FavoriteState

    data class Empty(
        val message: String
    ): FavoriteState
}