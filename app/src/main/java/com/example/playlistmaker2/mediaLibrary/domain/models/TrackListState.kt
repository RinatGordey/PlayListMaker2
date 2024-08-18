package com.example.playlistmaker2.mediaLibrary.domain.models

import com.example.playlistmaker2.search.domain.model.Track

sealed interface TrackListState {

    data class NoContent(
        val totalTime: String,
        val playlist: Playlist,
    ) : TrackListState

    data class TrackListContent(
        val tracks: List<Track>,
        val totalTime: String,
        val playlist: Playlist,
    ) : TrackListState
}