package com.example.playlistmaker2.db.domain.db

import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun favoriteTracks(): Flow<List<Track>>

    suspend fun addFavorite(track: Track)

    suspend fun isFavorite(trackId: Int): Boolean

    suspend fun deleteFavorite(track: Track)
}