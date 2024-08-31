package com.example.playlistmaker2.db.data.impl

import com.example.playlistmaker2.db.AppDatabase
import com.example.playlistmaker2.db.data.converters.PlaylistDbConvertor
import com.example.playlistmaker2.db.data.converters.TrackDbConvertor
import com.example.playlistmaker2.db.data.entity.PlaylistEntity
import com.example.playlistmaker2.db.domain.db.PlaylistRepository
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackDbConvertor: TrackDbConvertor,
) : PlaylistRepository {

    override suspend fun addToPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override fun getPlaylist(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylist()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlistDbConvertor.map(it) }
    }

    override suspend fun addTrackToPlaylist(track: Track) {
        appDatabase.playlistTrackDao().insertTrack(playlistDbConvertor.mapTrack(track))
    }

    override fun getTracksPlaylist(id: List<Int>): Flow<List<Track>> = flow {
        emit(tracksList(id))
    }

    override suspend fun getCurrentPlaylist(id: Long): Playlist {
        return playlistDbConvertor.map(appDatabase.playlistDao().getCurrentPlaylist(id))
    }

    override fun deleteTrack(idPL: Long, idTrack: Int): Flow<List<Track>> = flow {
        val pl = appDatabase.playlistDao().getCurrentPlaylist(idPL)
        val idList = playlistDbConvertor.mapToList(pl.tracksId) as MutableList
        for (item in idList) {
            if (idTrack == item) {
                idList.remove(idTrack)
                break
            }
        }
        val newTracksId = if (idList.isNotEmpty()) {
            playlistDbConvertor.mapToJson(idList)
        } else {
            ""
        }
            val count = pl.tracksCount - 1
            val newPL = Playlist(
                pl.playlistId,
                pl.playlistName,
                pl.playlistDescription,
                pl.uri,
                newTracksId,
                count,
            )
            addToPlaylist(newPL)

        if (!checkingPlaylists(idTrack)) {
            val currentTrack = appDatabase.playlistTrackDao().getCurrentTrack(idTrack)
            appDatabase.playlistTrackDao().deleteTrack(currentTrack)
        }
        emit(tracksList(idList))
    }

    private suspend fun tracksList(id: List<Int>): List<Track> {
        val currentPLTracks = ArrayList<Track>()
        val allTracks = appDatabase.playlistTrackDao().getTracks()

        for (item in id) {
            for (track in allTracks) {
                if (item == track.trackId) {
                    currentPLTracks.add(trackDbConvertor.map(track))
                }
            }
        }
        return currentPLTracks
    }

    private suspend fun checkingPlaylists(id: Int): Boolean {
        var isInPlaylist = false
        val plList = appDatabase.playlistDao().getPlaylist()
        for (item in plList) {
            val idTracks = playlistDbConvertor.mapToList(item.tracksId) as? MutableList
            if (idTracks != null) {
                if (idTracks.contains(id)) {
                    isInPlaylist = true
                    break
                }
            }
        }
        return isInPlaylist
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun deletePlaylist(playlist: Playlist) {
        if (playlist.tracksId != "") {
            val tracksForDelete = playlistDbConvertor.mapToList(playlist.tracksId)
            GlobalScope.launch {
                appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))

                for (item in tracksForDelete) {
                    if (!checkingPlaylists(item)) {
                        val currentTrack = appDatabase.playlistTrackDao().getCurrentTrack(item)
                        appDatabase.playlistTrackDao().deleteTrack(currentTrack)
                    }
                }
            }
        }

        else {
            appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }
}