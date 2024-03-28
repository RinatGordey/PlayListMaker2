package com.example.playlistmaker2.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.api.HistoryInteractor
import com.example.playlistmaker2.search.domain.api.SearchInteractor
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.models.TrackSearchState

class TrackSearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor,
    ) : AndroidViewModel(application) {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

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
                    val tracks = foundTracks?.toMutableList() ?: mutableListOf()

                    when {
                        errorMessage == "Internet" -> {
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
            })
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