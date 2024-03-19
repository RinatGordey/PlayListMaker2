package com.example.playlistmaker2.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker2.search.domain.api.HistoryRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson

const val HISTORY_PREFERENCES = "history_preferences"
const val HISTORY_KEY = "key_for_history"

class HistoryRepositoryImpl(val context: Context) : HistoryRepository {
    val sharedPrefs = context.getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)


    override fun getTracks(): Array<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY, null)
            ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun addTrack(track: Track): List<Track> {
        val searchHistoryList = getTracks().toMutableList()
        val index = searchHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (index != -1) {
            searchHistoryList.removeAt(index)
            searchHistoryList.add(0, track)
        } else if (searchHistoryList.size < 10) {
            searchHistoryList.add(0, track)
        } else {
            searchHistoryList.removeAt(9)
        }
        write(searchHistoryList)

        return searchHistoryList
    }

    override fun cleanHistory() {
        sharedPrefs.edit()
            .clear()
            .apply()
    }

    private fun write(tracks: MutableList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
}