package com.example.playlistmaker2.db.data.converters

import com.example.playlistmaker2.db.data.entity.PlaylistEntity
import com.example.playlistmaker2.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            playlist.tracksId,
            playlist.tracksCount,
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            playlist.tracksId,
            playlist.tracksCount,
        )
    }

    fun mapToJson(idList: MutableList<Int>): String {
        val json: String = Gson().toJson(idList)
        return json
    }

    fun mapToList(idList: String?): List<Int> {
        return idList?.let {
            val type = object : TypeToken<List<Int>>() {}.type
            Gson().fromJson<List<Int>>(it, type)
        } ?: emptyList()
    }

    fun mapTrack(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
        )
    }
}