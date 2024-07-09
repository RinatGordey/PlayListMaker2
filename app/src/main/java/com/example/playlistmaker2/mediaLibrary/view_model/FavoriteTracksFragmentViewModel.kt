package com.example.playlistmaker2.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.db.domain.db.FavoriteInteractor
import com.example.playlistmaker2.mediaLibrary.domain.models.FavoriteState
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteTracksFragmentViewModel(
    private val favoriteInteractor: FavoriteInteractor,
): ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()

    init {
        fillData()
    }

    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun fillData() {
        viewModelScope.launch {
            favoriteInteractor.favoriteTracks().collect { tracks ->
                getState(tracks)}
        }
    }

    private fun getState(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            stateLiveData.postValue(FavoriteState.Empty)
        } else {
            stateLiveData.postValue(FavoriteState.Content(tracks))
        }
    }
}