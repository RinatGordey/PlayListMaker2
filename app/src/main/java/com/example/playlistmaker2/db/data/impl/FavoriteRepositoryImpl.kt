package com.example.playlistmaker2.db.data.impl

import com.example.playlistmaker2.db.AppDatabase
import com.example.playlistmaker2.db.data.converters.TrackDbConvertor
import com.example.playlistmaker2.db.data.entity.FavoriteEntity
import com.example.playlistmaker2.db.domain.db.FavoriteRepository
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
): FavoriteRepository {

    override fun favoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromFavoriteEntity(tracks))
    }

    override suspend fun addFavorite(track: Track) {
        val favoriteEntity = FavoriteEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTime.toIntOrNull() ?: 0,
            artworkUrl100 = track.artworkUrl100,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
        appDatabase.trackDao().insertTracks(listOf(favoriteEntity))
    }

    override suspend fun isFavorite(trackId: Int): Boolean =
        appDatabase.trackDao().isFavorite(trackId)

    override suspend fun deleteFavorite(track: Track) {
        val favoriteEntity = FavoriteEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTime.toIntOrNull() ?: 0,
            artworkUrl100 = track.artworkUrl100,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
        appDatabase.trackDao().deleteTrack(favoriteEntity)
    }

    private fun convertFromFavoriteEntity(tracks: List<FavoriteEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}