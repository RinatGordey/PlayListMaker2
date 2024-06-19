package com.example.playlistmaker2.db.domain.impl

import com.example.playlistmaker2.db.domain.db.FavoriteInteractor
import com.example.playlistmaker2.db.domain.db.FavoriteRepository
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
): FavoriteInteractor {

    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.favoriteTracks()
    }

    override suspend fun addFavorite(track: Track) {
        favoriteRepository.addFavorite(track)
    }

    override suspend fun isFavorite(trackId: Int): Boolean =
        favoriteRepository.isFavorite(trackId)


    override suspend fun deleteFavorite(track: Track) {
        favoriteRepository.deleteFavorite(track)
    }
}