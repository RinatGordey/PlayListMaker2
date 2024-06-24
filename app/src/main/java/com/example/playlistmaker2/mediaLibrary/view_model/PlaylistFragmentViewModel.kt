package com.example.playlistmaker2.mediaLibrary.view_model

import android.app.Application
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.PlaylistInteractor
import com.example.playlistmaker2.mediaLibrary.models.Playlist
import com.example.playlistmaker2.mediaLibrary.models.PlaylistState
import com.example.playlistmaker2.mediaLibrary.models.PlaylistToRv
import kotlinx.coroutines.launch
import java.io.File

class PlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val application: Application,
) : ViewModel() {

    companion object {
        const val MY_IMAGE = "my image"
    }

    private val stateLiveData = MutableLiveData<PlaylistState>()

    init {
        getData()
    }

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private fun getData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect { playlists -> getState(playlists) }
        }
    }

    private fun getState(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            stateLiveData.postValue(PlaylistState.NoPlaylists)

        } else {
            val playlistForRV = mutableListOf<PlaylistToRv>()
            for (item in playlists) {
                playlistForRV.add(mapToList(item))
            }

            stateLiveData.postValue(PlaylistState.PlaylistsContent(playlistForRV))
        }
    }

    private fun mapToList(playlist: Playlist): PlaylistToRv {
        var file: File? = null
        if (playlist.uri != "") {
            val filePath =
                File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_IMAGE)
            file = playlist.uri?.let { File(filePath, it) }
        }

        return PlaylistToRv(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            file,
            playlist.tracksId,
            playlist.tracksCount,
        )
    }
}