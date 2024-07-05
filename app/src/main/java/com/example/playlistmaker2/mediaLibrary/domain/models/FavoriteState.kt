package com.example.playlistmaker2.mediaLibrary.domain.models

import com.example.playlistmaker2.search.domain.model.Track

sealed interface FavoriteState {

    data class Content(
        val tracks: List<Track>
    ): FavoriteState

    data object Empty : FavoriteState
}