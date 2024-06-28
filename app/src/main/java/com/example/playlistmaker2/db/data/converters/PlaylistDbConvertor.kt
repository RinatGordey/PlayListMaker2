package com.example.playlistmaker2.db.data.converters

import com.example.playlistmaker2.db.data.entity.PlaylistEntity
import com.example.playlistmaker2.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            convertListToJson(playlist.tracksId),
            playlist.tracksCount,
        )
    }

    fun map(playlist: PlaylistEntity): Playlist{
        return Playlist(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.uri,
            convertJsonToList(playlist.tracksId),
            playlist.tracksCount,
        )
    }

    private fun convertListToJson(trackIdList: List<Int>): String {
        val gsonBuilder = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        val gson = gsonBuilder.create()
        return gson.toJson(trackIdList, object : TypeToken<List<Int>?>() {}.type)
    }

    private fun convertJsonToList(trackIdList: String?): List<Int> {
        if (trackIdList.isNullOrEmpty()) {
            return emptyList()
        }
        val gsonBuilder = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        val gson = gsonBuilder.create()
        return gson.fromJson(trackIdList, List::class.java) as List<Int>
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