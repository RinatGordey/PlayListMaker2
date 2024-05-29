package com.example.playlistmaker2.search.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.api.HistoryInteractor
import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.models.TrackSearchState
import com.example.playlistmaker2.util.ErrorType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor,
    ) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeState(): LiveData<TrackSearchState> = stateLiveData

    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchRequest(newSearch: String) {
        if (newSearch.isNotEmpty()) {
            renderState(TrackSearchState.Loading)

            viewModelScope.launch {
                searchInteractor.searchTracks(newSearch).collect { pair ->
                    searchResults(pair.first, pair.second)
                }
            }
        }
    }

    private fun searchResults(foundTrack: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTrack != null)
        tracks.addAll(foundTrack)
        when {
            errorMessage == ErrorType.INTERNET.message -> {
                renderState(
                    TrackSearchState.Error(
                        getApplication<Application>().getString(R.string.error_to_link)
                    )
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    TrackSearchState.Empty(
                        getApplication<Application>().getString(R.string.error_tracks)
                    )
                )
            }

            else -> {
                renderState(
                    TrackSearchState.SearchContent(
                        tracks
                    )
                )
            }
        }
    }

    private fun renderState(state: TrackSearchState) {
        stateLiveData.postValue(state)
    }

    fun historyAddTrack(track: Track) {
        val newHistoryList = historyInteractor.addTrack(track).toMutableList()
        renderState(
            TrackSearchState.HistoryContent(
                newHistoryList, false
            )
        )
    }

    fun getHistory() {
        val historyTracks: MutableList<Track> = historyInteractor.getTracks().toMutableList()
        renderState(
            TrackSearchState.HistoryContent(
                historyTracks, false
            )
        )
    }

    fun cleanHistory() {
        historyInteractor.cleanHistory()
        renderState(
            TrackSearchState.HistoryContent(
                ArrayList(), false
            )
        )
    }
}