package com.example.playlistmaker2.search.data

import android.content.SharedPreferences
import com.example.playlistmaker2.search.domain.api.HistoryRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson

private const val HISTORY_KEY = "key_for_history"
private const val NUMBER_TEN = 10
private const val NUMBER_NINE = 9
private const val NUMBER_ZERO = 0

class HistoryRepositoryImpl(private val sharedPrefs: SharedPreferences,
    private val gson: Gson,
    ) : HistoryRepository {

    override fun getTracks(): Array<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY, null)
            ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    override fun addTrack(track: Track): List<Track> {
        val searchHistoryList = getTracks().toMutableList()
        val index = searchHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (index != -1) {
            searchHistoryList.removeAt(index)
            searchHistoryList.add(NUMBER_ZERO, track)
        } else if (searchHistoryList.size < NUMBER_TEN) {
            searchHistoryList.add(NUMBER_ZERO, track)
        } else {
            searchHistoryList.removeAt(NUMBER_NINE)
        }
        write(searchHistoryList)

        return searchHistoryList
    }

    override fun cleanHistory() {
        sharedPrefs.edit()
            .clear()
            .apply()
    }

    private fun write(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
}