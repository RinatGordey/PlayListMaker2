package com.example.playlistmaker2.db.data.converters

import com.example.playlistmaker2.db.data.entity.FavoriteEntity
import com.example.playlistmaker2.search.data.dto.TrackDto
import com.example.playlistmaker2.search.domain.model.Track

class TrackDbConvertor {

    fun map(track: TrackDto): FavoriteEntity {
        return FavoriteEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.previewUrl,
        )
    }

    fun map(track: FavoriteEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis.toString(),
            track.artworkUrl100,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName.toString(),
            track.country,
            track.previewUrl,
        )
    }
}