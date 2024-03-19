package com.example.playlistmaker2.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.models.TrackSearchState
import com.example.playlistmaker2.util.Creator

class TrackSearchViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackSearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    private val searchInteractor = Creator.provideSearchInteractor(getApplication())
    private val historyInteractor = Creator.provideHistoryInteractor(getApplication())
    private val handler = Handler(Looper.getMainLooper())


    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeState(): LiveData<TrackSearchState> = stateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackSearchState.Loading)

            searchInteractor.searchTracks(newSearchText, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    val tracks = mutableListOf<Track>()
                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)
                    }

                    when {
                        errorMessage == "Internet" -> {
                            renderState(
                                TrackSearchState.Error(
                                    getApplication<Application>().getString(R.string.something_went_wrong)
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
            })
        }
    }

    private fun renderState(state: TrackSearchState) {
        stateLiveData.postValue(state)
    }

    fun historyAddTrack(track: Track) {
        val newHistoryList = historyInteractor.addTrack(track) as ArrayList<Track>
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