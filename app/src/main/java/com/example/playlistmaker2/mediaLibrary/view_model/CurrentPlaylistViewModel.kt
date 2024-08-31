package com.example.playlistmaker2.mediaLibrary.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.R
import com.example.playlistmaker2.db.data.converters.PlaylistDbConvertor
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.domain.models.Playlist
import com.example.playlistmaker2.mediaLibrary.domain.models.TrackListState
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.sharing.domain.api.ExternalNavigator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class CurrentPlaylistViewModel(
    id: Long?,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val externalNavigator: ExternalNavigator,
    private val context: Context,
) : ViewModel() {

    private val currentPLTracksLiveData = MutableLiveData<TrackListState>()

    init {
        getPlaylist(id)
    }

    fun getPlaylist(id: Long?) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getCurrentPlaylist(id!!)
            if (playlist.tracksId != "") {
                val idList = playlistDbConvertor.mapToList(playlist.tracksId) as MutableList
                playlistInteractor.getTracksPlaylist(idList)
                    .collect { tracks -> getState(tracks, playlist) }
            } else {
                getState(emptyList(), playlist)
            }
        }
    }

    fun getCurrentPLTracksLiveData(): LiveData<TrackListState> = currentPLTracksLiveData


    private fun getState(tracks: List<Track>, playlist: Playlist) {
        if (tracks.isEmpty()) {
            currentPLTracksLiveData.postValue(TrackListState.NoContent("0", playlist))
        } else {
            var totalTime = 0
            tracks.forEach { track ->
                val units = (track.trackTime!!).split(':')
                val newDuration = (units[0].toInt() * 60) + units[1].toInt()
                totalTime += newDuration
            }
            totalTime *= 1000

            val minutes = (totalTime / 60000) % 60
            val seconds = (totalTime / 1000) % 60

            val formattedTime = String.format("%02d:%02d", minutes, seconds)

            currentPLTracksLiveData.postValue(
                TrackListState.TrackListContent(
                    tracks, formattedTime, playlist
                )
            )
        }
    }

    fun deleteTrack(plID: Long, trackID: Int) {

        viewModelScope.launch {
            playlistInteractor.deleteTrack(plID, trackID)
                .collect { tracks -> getState(tracks, playlistInteractor.getCurrentPlaylist(plID)) }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }

    fun share(playlist: Playlist, tracks: List<Track>) {
        val count = playlist.tracksCount
        val trackPlural = context.resources.getQuantityString(R.plurals.plurals_2, count, count)
        val message = StringBuilder()
        message.append("${playlist.playlistName}\n")
            .append("${playlist.playlistDescription}\n")
            .append("$trackPlural\n")

        tracks.forEachIndexed { index, track ->
            message.append("${index + 1}. ${track.artistName} - ${track.trackName} [${track.trackTime}]\n")
        }
        externalNavigator.shareLink(message.toString())
    }
}