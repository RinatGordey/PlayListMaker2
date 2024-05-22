package com.example.playlistmaker2.db.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.R
import com.example.playlistmaker2.db.domain.db.FavoriteInteractor
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val context: Context,
    private val favoriteInteractor: FavoriteInteractor,
): ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()

    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun fillData() {
        renderState(FavoriteState.Loading)
        viewModelScope.launch {
            favoriteInteractor
                .favoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty(context.getString(R.string.error_tracks)))
        } else {
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        stateLiveData.postValue(state)
    }
}